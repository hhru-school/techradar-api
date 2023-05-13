package ru.hh.techradar.security.exception;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import org.springframework.security.authentication.BadCredentialsException;
import ru.hh.techradar.exception.Error;

public class BadCredentialsExceptionMapper implements ExceptionMapper<BadCredentialsException> {

  @Override
  public Response toResponse(BadCredentialsException exception) {
    return Response.status(Response.Status.FORBIDDEN).
        entity(new Error(exception.getMessage(), Response.Status.FORBIDDEN)).
        type(MediaType.APPLICATION_JSON).
        build();
  }
}
