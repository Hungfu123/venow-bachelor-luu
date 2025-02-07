package org.venow.backend.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.venow.backend.service.UserService;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
@EnableMethodSecurity(securedEnabled = true)
@Profile("!testpyramid-testcontainer & !test & !testpyramid-h2")
public class SecurityConfig {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests


                                .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()
                                .requestMatchers("/auth/login", "/auth/register").permitAll()
                                .requestMatchers("/exhibitor/**").hasRole("EXHIBITOR")
                                .requestMatchers("/organizer/**").hasRole("ORGANIZER")
                                .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .successHandler(customAuthenticationSuccessHandler)
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                ).sessionManagement(session -> session
                        .sessionFixation().migrateSession()
                );

        return http.build();
    }


    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userService);
        provider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(provider);
    }
}
