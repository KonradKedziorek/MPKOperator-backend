package pl.kedziorek.mpkoperator.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import pl.kedziorek.mpkoperator.domain.Role;
import pl.kedziorek.mpkoperator.domain.User;
import pl.kedziorek.mpkoperator.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Component
public class AuthenticationProviderService implements AuthenticationProvider {

    private final UserRepository userRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();

        List<SimpleGrantedAuthority> updatedAuthorities = new ArrayList<>();
        User user = userRepository.findByUsername(email)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + email));
        Set<Role> roleSet = user.getRoles();
        roleSet.forEach(role -> {
            updatedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        authentication = new UsernamePasswordAuthenticationToken(email, password, updatedAuthorities);
        return authentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
