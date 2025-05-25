package org.magnuschase.pkchart.config;

import org.magnuschase.pkchart.model.Role;
import org.magnuschase.pkchart.security.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
  @Autowired private JwtAuthFilter jwtAuthFilter;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger")
                    .permitAll()
                    .requestMatchers("/auth/login", "/auth/register")
                    .permitAll()
                    .requestMatchers("/card/add", "/card/remove")
                    .hasRole(Role.ADMIN.name())
                    .requestMatchers("/set/add", "/set/remove")
                    .hasRole(Role.ADMIN.name())
                    .requestMatchers("/requests/all", "/requests/approve/**", "/requests/reject/**")
                    .hasRole(Role.ADMIN.name())
                    .requestMatchers("/prices/update/**", "/prices/update-multiple")
                    .hasRole(Role.ADMIN.name())
                    .requestMatchers("/set/all", "/card/all")
                    .authenticated()
                    .anyRequest()
                    .authenticated())
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new Argon2PasswordEncoder(16, 32, 1, 65536, 3);
  }
}
