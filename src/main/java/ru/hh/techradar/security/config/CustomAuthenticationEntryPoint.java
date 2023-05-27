package ru.hh.techradar.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.IOException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import ru.hh.techradar.enumeration.ExceptionType;
import ru.hh.techradar.exception.Error;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private final ObjectMapper objectMapper;

  public CustomAuthenticationEntryPoint(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException exception) throws IOException {
    Error error = new Error(exception.getMessage(), Response.Status.UNAUTHORIZED, ExceptionType.UNAUTHORIZED);
    response.setStatus(Response.Status.UNAUTHORIZED.getStatusCode());
    response.setContentType(MediaType.APPLICATION_JSON);
    response.getWriter().write(objectMapper.writeValueAsString(error));
  }
}
