package ru.hh.techradar.security.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.hh.techradar.entity.User;
import static ru.hh.techradar.security.dto.Constants.AUTHORIZATION;
import static ru.hh.techradar.security.dto.Constants.TOKEN_TYPE;
import ru.hh.techradar.security.model.CustomUserDetails;
import ru.hh.techradar.security.service.TokenService;
import ru.hh.techradar.service.UserService;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {

  private final TokenService tokenService;
  private final UserService userService;

  public AuthenticationFilter(TokenService tokenService, UserService userService) {
    this.tokenService = tokenService;
    this.userService = userService;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    String authHeader = request.getHeader(AUTHORIZATION);
    if (Objects.isNull(authHeader) || !authHeader.startsWith(TOKEN_TYPE)) {
      filterChain.doFilter(request, response);
      return;
    }
    String token = authHeader.substring(7);
    String username = tokenService.extractUsername(token);
    response.addCookie(new Cookie("username", username));
    if (Objects.nonNull(username) && Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
      User user = userService.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found!"));
      UserDetails userDetails = CustomUserDetails.toCustomUserDetails(user);
      if (tokenService.isTokenValid(token, userDetails)) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
            userDetails,
            null,
            userDetails.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
      }
    }
    filterChain.doFilter(request, response);
  }
}
