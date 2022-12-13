package pl.kedziorek.mpkoperator.config.security;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import pl.kedziorek.mpkoperator.config.AuthTokenFilter;
import pl.kedziorek.mpkoperator.config.JwtUtils;
import pl.kedziorek.mpkoperator.service.impl.UserDetailsServiceImpl;

import java.util.Arrays;
import java.util.List;

@Configuration
@Slf4j
@EnableGlobalMethodSecurity(
        prePostEnabled = true
)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtUtils jwtUtils;

    private final UserDetailsServiceImpl userDetailsServiceimpl;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter(jwtUtils, userDetailsServiceimpl);
    }

    protected void configure(HttpSecurity http) throws Exception {
        http.cors()
                .configurationSource(corsConfigurationSource())
                .and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/auth/**").permitAll();
//              .antMatchers("/applications/**").hasRole("TRANSFER_MANAGER");
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedHeaders(Arrays.asList(
                "Content-Type",
                "Access-Control-Allow-Origin",
                "Access-Control-Allow-Headers",
                "Authorization",
                "Accept-Language"));
        config.setAllowedOrigins(List.of(""));
        config.setAllowedMethods(List.of("*"));
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
