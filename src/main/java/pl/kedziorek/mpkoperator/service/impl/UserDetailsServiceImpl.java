package pl.kedziorek.mpkoperator.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.kedziorek.mpkoperator.config.UserDetailsImpl;
import pl.kedziorek.mpkoperator.config.UserInfo;
import pl.kedziorek.mpkoperator.domain.Role;
import pl.kedziorek.mpkoperator.domain.User;
import pl.kedziorek.mpkoperator.repository.UserRepository;

import java.util.Set;


@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(email)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + email));
        UserInfo userInfo = UserInfo.builder()
                .username(user.getUsername())
                .roles((Set<Role>) user.getRoles())
                .build();
        return UserDetailsImpl.build(userInfo);
    }
}