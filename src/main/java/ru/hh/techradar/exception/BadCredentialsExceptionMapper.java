package ru.hh.techradar.exception;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import org.springframework.security.authentication.BadCredentialsException;

public class BadCredentialsExceptionMapper implements ExceptionMapper<BadCredentialsException> {

  @Override
  public Response toResponse(BadCredentialsException exception) {
    return Response.status(Response.Status.FORBIDDEN).
        //todo заменить на exception.getMessage()
        entity(new Error("Неверный логин или пароль!", Response.Status.FORBIDDEN)).
        type(MediaType.APPLICATION_JSON).
        build();
  }
}
