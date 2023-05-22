package ru.hh.techradar.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
  private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
  private final LogoutService logoutService;

  public SecurityConfig(
      AuthenticationFilter authenticationFilter,
      AuthenticationProvider authenticationProvider,
      CustomAuthenticationEntryPoint customAuthenticationEntryPoint,
      LogoutService logoutService) {
    this.authenticationFilter = authenticationFilter;
    this.authenticationProvider = authenticationProvider;
    this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
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
    http
        .exceptionHandling()
        .authenticationEntryPoint(customAuthenticationEntryPoint);
    return http.build();
  }

}
