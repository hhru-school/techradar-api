package ru.hh.techradar.security.exception;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import ru.hh.techradar.exception.Error;

public class ExpiredJwtExceptionMapper implements ExceptionMapper<ExpiredJwtException> {

  @Override
  public Response toResponse(ExpiredJwtException exception) {
    return Response.status(Response.Status.FORBIDDEN).
        entity(new Error(exception.getMessage(), Response.Status.FORBIDDEN)).
        type(MediaType.APPLICATION_JSON).
        build();
  }
}
