package pl.kedziorek.mpkoperator.api;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.kedziorek.mpkoperator.config.JwtUtils;
import pl.kedziorek.mpkoperator.domain.Complaint;
import pl.kedziorek.mpkoperator.domain.Role;
import pl.kedziorek.mpkoperator.domain.User;
import pl.kedziorek.mpkoperator.domain.dto.Credentials;
import pl.kedziorek.mpkoperator.domain.dto.RoleToUserDTO;
import pl.kedziorek.mpkoperator.domain.dto.request.*;
import pl.kedziorek.mpkoperator.domain.dto.response.*;
import pl.kedziorek.mpkoperator.service.AuthService;
import pl.kedziorek.mpkoperator.service.EmailService;
import pl.kedziorek.mpkoperator.service.UserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static pl.kedziorek.mpkoperator.domain.Complaint.mapToComplaintDetailsResponse;
import static pl.kedziorek.mpkoperator.domain.User.mapToUserDetailsResponse;
import static pl.kedziorek.mpkoperator.utils.CookieUtils.buildCookie;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final AuthService authenticate;
    private final JwtUtils jwtUtils;

    @Value("${domain}")
    private String domain;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Credentials credentials, HttpServletResponse response) throws IllegalAccessException {
        AuthResponse authResponse = authenticate.authenticate(credentials);
        Cookie tokenCookie = buildCookie(7 * 24 * 60 * 60, true, true, "/", domain, authResponse.getToken(), "token");
        Cookie refreshTokenCookie= buildCookie(7 * 24 * 60 * 60, true, true, "/", domain, authResponse.getRefreshToken(), "refreshToken");
        response.addCookie(tokenCookie);
        response.addCookie(refreshTokenCookie);
        return ResponseEntity.ok(authResponse);
    }

//    @PostMapping("/user/save")
//    public ResponseEntity<User> saveUser(
//            @Valid @RequestPart CreateUserRequest user,
//            @RequestParam(value = "file", required = false) MultipartFile multipartFile
//            ) throws IOException {
//        return ResponseEntity.ok().body(userService.saveUser(user, multipartFile));
//    }

    @PostMapping("/user/save")
    public ResponseEntity<?> saveUser(@Valid @RequestBody UserRequest user) {
        return ResponseEntity.ok().body(userService.saveUser(user));
    }

    @PutMapping("/user/resetPassword")
    public ResponseEntity<User> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
        return ResponseEntity.ok().body(userService.resetPassword(resetPasswordRequest));
    }

    //To metoda dla usera jak chce zmienic hasło
    @PutMapping("/user/uuid={uuid}/updatePassword")
    public ResponseEntity<User> updatePassword(
            @RequestBody UpdateUsersPasswordRequest usersPasswordRequest,
            @PathVariable UUID uuid) {
        return ResponseEntity.ok().body(userService.updateUsersPassword(usersPasswordRequest, uuid));
    }

    //To metoda do zmiany danych dostępna dla admina
    @PutMapping("/user/uuid={uuid}/edit")
    public ResponseEntity<User> editUserDataByAdmin(
            @RequestBody UpdateUserDataByAdminRequest updateUserDataByAdminRequest,
            @PathVariable UUID uuid) {
        return ResponseEntity.ok().body(userService.editUserDataByAdmin(updateUserDataByAdminRequest, uuid));
    }

    @PostMapping("/user/uuid={uuid}/sendMailFromUserData")
    public ResponseEntity<SimpleMailMessage> sendMailFromUserData(
            @Validated
            @RequestBody EmailToUserRequest emailToUserRequest,
            @PathVariable UUID uuid) {
        return ResponseEntity.ok().body(userService.sendMailFromUserDetails(emailToUserRequest, uuid));
    }

    // pobranie detailsow usera - konkretnego (metoda dla admina i  usera zalogowanego na swoje konto)
    @GetMapping("/user/uuid={uuid}")
    public ResponseEntity<UserDetailsResponse> getUser(@PathVariable UUID uuid) {
        User user = userService.findByUuid(uuid);
        return ResponseEntity.ok().body(mapToUserDetailsResponse(user));
    }

    @PostMapping("/users/page={page}/size={size}")
    public ResponseEntity<?> getUsers(
            @PathVariable int page,
            @PathVariable int size,
            @RequestBody Map<String, String> params) {
        DataResponse<User> userDataResponse = userService.getUsers(params, page, size);
        List<UserResponse> userList = userDataResponse.getData().stream().map(User::responseMap).collect(Collectors.toList());

        return ResponseEntity.ok().body(DataResponse.<UserResponse>builder()
                .data(userList)
                .page(userDataResponse.getPage())
                .size(userDataResponse.getSize())
                .build()
        );
    }

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refresh_token = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refresh_token);
                String username = decodedJWT.getSubject();
                User user = userService.getUser(username);

                String access_token = JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                        .sign(algorithm);

                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", access_token);
                tokens.put("refresh_token", refresh_token);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            } catch (Exception exception){
                log.error("Error", exception);
            }
        } else {
            throw new RuntimeException("Refresh token is missing");
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response, HttpServletRequest request) {
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setDomain(domain);
        response.addCookie(cookie);
        return ResponseEntity.ok(cookie);
    }

    @GetMapping("/logged-in")
    public ResponseEntity<?> loggedIn(HttpServletRequest request){
        boolean loggedIn;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            Optional<Cookie> optionalCookie = Arrays.stream(cookies).filter(cookie -> cookie.getName().equals("token")).findFirst();
            String token = optionalCookie.map(Cookie::getValue).orElse(null);
            loggedIn = jwtUtils.validateJwtToken(token);
            return ResponseEntity.ok(
                    LoggedInResponse.
                            builder().
                            loggedIn(
                                    loggedIn
                            ).build()
            );
        }
        return ResponseEntity.ok(
                LoggedInResponse.
                        builder().
                        loggedIn(
                                false
                        ).build()
        );
    }
}
