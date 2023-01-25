package pl.kedziorek.mpkoperator.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.kedziorek.mpkoperator.config.JwtUtils;
import pl.kedziorek.mpkoperator.config.security.AuthenticationProviderService;
import pl.kedziorek.mpkoperator.domain.User;
import pl.kedziorek.mpkoperator.domain.dto.response.AuthResponse;
import pl.kedziorek.mpkoperator.domain.dto.Credentials;
import pl.kedziorek.mpkoperator.service.AuthService;
import pl.kedziorek.mpkoperator.service.UserService;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final AuthenticationProviderService authenticationProviderService;

    private final JwtUtils jwtUtils;

    @Override
    public AuthResponse authenticate(Credentials credentials) throws IllegalAccessException {
        User user = userService.getUser(credentials.getUsername());
        String password = credentials.getPassword();
        if(user!=null && (bCryptPasswordEncoder.matches(password,
                user.getPassword()))){
            Authentication authentication = databaseAuthenticate(credentials);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);
            String refreshToken = jwtUtils.generateRefreshToken(authentication);
            List<String> roles = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
            return AuthResponse.builder()
                    .token(jwt)
                    .username(authentication.getName())
                    .refreshToken(refreshToken)
                    .roles(roles)
                    .build();
        }
        throw new IllegalAccessException("");
    }

    private Authentication databaseAuthenticate(Credentials credentials) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword());
        return authenticationProviderService.authenticate(usernamePasswordAuthenticationToken);
    }
}
