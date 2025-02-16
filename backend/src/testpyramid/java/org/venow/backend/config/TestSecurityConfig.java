package org.venow.backend.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.TestingAuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.venow.backend.domain.model.Role;
import org.venow.backend.domain.model.User;
import org.venow.backend.service.UserRepository;
import org.venow.backend.service.dto.UserDTO;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.config.Customizer.withDefaults;

@TestConfiguration
public class TestSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChainTestPyramid(HttpSecurity http, CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                        .requestMatchers("/health").authenticated()
                        .anyRequest().permitAll())
                .httpBasic(withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(form -> form
                        .successHandler(customAuthenticationSuccessHandler)
                        .permitAll())
                .securityMatcher("/**");

        return http.build();
    }
    @Bean
    public CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler();
    }

    @Bean
    @Primary
    public UserRepository userRepositoryTestPyramid() {
        UserRepository mockRepository = mock(UserRepository.class);

        UserDTO testUser = new UserDTO(1L,
                UUID.randomUUID(),
                "Test",
                new BCryptPasswordEncoder().encode("password123"),
                "organizer3@example.com",
                "ORGANIZER"
        );

        when(mockRepository.findByEmail("organizer3@example.com"))
                .thenReturn(Optional.of(testUser));

        return mockRepository;
    }

    @Bean
    @Primary
    public AuthenticationManager authenticationManagerTestPyramid(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(provider);
    }


    @Bean
    @Primary
    public UserDetailsService userDetailsServiceTestPyramid(UserRepository userRepository) {
        return username -> {
            System.out.println("Trying to authenticate: " + username);
            return userRepository.findByEmail(username)
                    .map(user -> {
                        System.out.println("User found: " + user.email());
                        return new CustomUserDetails(
                                user.userId(),
                                user.email(),
                                user.password(),
                                Collections.singleton(() -> "ROLE_" + user.role())
                        );
                    })
                    .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        };
    }




    @Bean
    public PasswordEncoder passwordEncoderTest() {
        return new BCryptPasswordEncoder();
    }


}
