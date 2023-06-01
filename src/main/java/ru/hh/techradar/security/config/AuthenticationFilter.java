package ru.hh.techradar.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.IOException;
import java.util.Objects;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.hh.techradar.entity.User;
import ru.hh.techradar.exception.Error;
import static ru.hh.techradar.security.dto.Constants.AUTHORIZATION;
import static ru.hh.techradar.security.dto.Constants.TOKEN_TYPE;
import static ru.hh.techradar.security.dto.Constants.TOKEN_TYPE_LENGTH;
import ru.hh.techradar.security.model.CustomUserDetails;
import ru.hh.techradar.security.service.TokenService;
import ru.hh.techradar.service.UserService;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {

  private final TokenService tokenService;
  private final UserService userService;
  private final ObjectMapper objectMapper;

  public AuthenticationFilter(
      TokenService tokenService,
      UserService userService,
      ObjectMapper objectMapper) {
    this.tokenService = tokenService;
    this.userService = userService;
    this.objectMapper = objectMapper;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain) throws IOException, ServletException {
    String authHeader = request.getHeader(AUTHORIZATION);
    if (Objects.isNull(authHeader) || !authHeader.startsWith(TOKEN_TYPE)) {
      filterChain.doFilter(request, response);
      return;
    }
    try {
      String token = authHeader.substring(TOKEN_TYPE_LENGTH);
      String username = tokenService.extractUsername(token);
      response.addCookie(new Cookie("username", username));
      if (Objects.nonNull(username) && Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
        User user = userService
            .findByUsername(username);
        UserDetails userDetails = CustomUserDetails.toCustomUserDetails(user);
        if (tokenService.isTokenValid(token, userDetails)) {
          UsernamePasswordAuthenticationToken authToken =
              new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
          authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(authToken);
        }
      }
    } catch (Exception exception) {
      write(response, exception);
      return;
    }
    filterChain.doFilter(request, response);
  }

  private void write(HttpServletResponse response, Exception exception) throws IOException {
    //todo заменить на exception.getMessage()
    Error error = new Error("Нет прав доступа!", Response.Status.FORBIDDEN);
    response.setStatus(Response.Status.FORBIDDEN.getStatusCode());
    response.setContentType(MediaType.APPLICATION_JSON);
    response.getWriter().write(objectMapper.writeValueAsString(error));
  }
}
