package com.yekj.csinquiry.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(c-> {
                    CorsConfigurationSource source = request -> {
                        CorsConfiguration config = new CorsConfiguration();
                        config.setAllowCredentials(true);
                        config.setAllowedOriginPatterns(List.of("http://localhost:5173"));
                        config.setAllowedMethods(List.of("GET", "POST"));
                        config.setAllowedHeaders(List.of("*"));
                        config.setExposedHeaders(List.of("*"));
                        return config;
                    };
                    c.configurationSource(source);
                })
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers(HttpMethod.POST,"/signup").permitAll()
                        .requestMatchers(HttpMethod.POST,"/signin").permitAll()
                ).httpBasic(withDefaults());

        return http.build();
    }
}
