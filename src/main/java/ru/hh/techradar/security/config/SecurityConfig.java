package ru.hh.techradar.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import static org.springframework.http.HttpMethod.GET;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.hh.techradar.security.service.LogoutService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

  private final AuthenticationFilter authenticationFilter;
  private final AuthenticationProvider authenticationProvider;
  private final LogoutService logoutService;

  public SecurityConfig(
      AuthenticationFilter authenticationFilter,
      AuthenticationProvider authenticationProvider, LogoutService logoutService) {
    this.authenticationFilter = authenticationFilter;
    this.authenticationProvider = authenticationProvider;
    this.logoutService = logoutService;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf()
        .disable()
        .authorizeHttpRequests()
        .requestMatchers("/api/auth/**")
        .permitAll()
        .requestMatchers(GET, "/api/tests/security/admin").hasAnyAuthority("ADMIN")
        .requestMatchers(GET, "/api/tests/security/user").hasAnyAuthority("USER")
        .anyRequest()
        .authenticated()
        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authenticationProvider(authenticationProvider)
        .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .logout()
        .logoutUrl("/api/auth/logout")
        .addLogoutHandler(logoutService)
        .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext());
    return http.build();
  }

}
