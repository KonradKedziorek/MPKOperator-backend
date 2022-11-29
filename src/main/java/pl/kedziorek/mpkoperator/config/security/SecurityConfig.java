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

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
//    }

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter(jwtUtils, userDetailsServiceimpl);
    }


//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
////        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean());
////        customAuthenticationFilter.setFilterProcessesUrl("/api/login");
//        http.csrf().disable();
//        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
////        http.authorizeRequests()
////                .antMatchers("/api/login/**", "/api/token/refresh/**").permitAll()
////                .antMatchers(GET, "/api/users/**", "/api/complaint/{uuid}/comments/**").permitAll()             //.hasAnyAuthority("ROLE_USER")   // TODO Tak przyznajemy permisje dla odpowiednich roli
////                .antMatchers(GET, "/api/role/addtouser").permitAll()   // TODO Tak przyznajemy permisje dla odpowiednich roli
////                .antMatchers(POST, "/api/user/save/**", "/api/complaint/save/**").permitAll()
////                .antMatchers(POST, "/api/complaint/{uuid}/comment/**").permitAll();
//        http.authorizeRequests().anyRequest().permitAll(); //zeby dodac autentykacje .authorization()
////        http.addFilter(customAuthenticationFilter);
//        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
//    }

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
        config.setAllowedHeaders(Arrays.asList("Content-Type", "Access-Control-Allow-Origin", " Access-Control-Allow-Headers", "Authorization", "Accept-Language"));
        config.setAllowedOrigins(List.of(""));
        config.setAllowedMethods(List.of("*"));
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
