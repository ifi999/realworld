package ifi.realworld.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                    .authorizeRequests()
                        .antMatchers(HttpMethod.POST, "/api/users/login", "/api/users").permitAll()
                        .antMatchers(HttpMethod.GET, "/api/profiles/**", "/api/articles/**", "/api/tags").permitAll()
                        .anyRequest().authenticated()
                .and()
                    .headers()
                        .frameOptions().sameOrigin()
                .and()
                    .csrf().disable()
                    .formLogin().disable()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//                .and() // TODO - jwt 설정 필요
//                .exceptionHandling()
//                .authenticationEntryPoint()
//                .accessDeniedHandler();

        return http.build();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
