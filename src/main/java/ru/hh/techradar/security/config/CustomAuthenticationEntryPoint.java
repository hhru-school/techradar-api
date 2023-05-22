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
import ru.hh.techradar.entity.exception.Error;

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
    //todo заменить на exception.getMessage()
    Error error = new Error("Нет прав доступа!", Response.Status.UNAUTHORIZED);
    response.setStatus(Response.Status.UNAUTHORIZED.getStatusCode());
    response.setContentType(MediaType.APPLICATION_JSON);
    response.getWriter().write(objectMapper.writeValueAsString(error));
  }
}
