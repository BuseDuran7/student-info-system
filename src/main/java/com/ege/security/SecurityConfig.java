package com.ege.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authz -> authz
                        // Public endpoints
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/user-roles/active").permitAll() // Frontend'in role listesine ihtiyacı olabilir

                        // Admin endpoints
                        .requestMatchers("/api/users/**").hasRole("ADMIN")
                        .requestMatchers("/api/user-roles/**").hasRole("ADMIN")
                        .requestMatchers("/api/programs/**").hasRole("ADMIN")

                        // Academic Staff endpoints - INSTRUCTOR eklendi
                        .requestMatchers("/api/academic-staff/**").hasAnyRole("ADMIN", "ACADEMIC_STAFF", "INSTRUCTOR")
                        .requestMatchers("/api/courses/**").hasAnyRole("ADMIN", "ACADEMIC_STAFF", "INSTRUCTOR")
                        .requestMatchers("/api/grades/**").hasAnyRole("ADMIN", "ACADEMIC_STAFF", "INSTRUCTOR")
                        .requestMatchers("/api/theses/**").hasAnyRole("ADMIN", "ACADEMIC_STAFF", "INSTRUCTOR")

                        // Student endpoints
                        .requestMatchers("/api/students/**").hasAnyRole("ADMIN", "ACADEMIC_STAFF", "INSTRUCTOR", "STUDENT")
                        .requestMatchers("/api/enrollments/**").hasAnyRole("ADMIN", "ACADEMIC_STAFF", "INSTRUCTOR", "STUDENT")

                        // Research Assistant endpoints
                        .requestMatchers("/api/research-assistants/**").hasAnyRole("ADMIN", "ACADEMIC_STAFF", "INSTRUCTOR", "RESEARCH_ASSISTANT")
                        .requestMatchers("/api/course-assistants/**").hasAnyRole("ADMIN", "ACADEMIC_STAFF", "INSTRUCTOR", "RESEARCH_ASSISTANT")

                        // Diğer tüm endpoint'ler authentication gerektirir
                        .anyRequest().authenticated()
                );

        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);
        return source;
    }
}