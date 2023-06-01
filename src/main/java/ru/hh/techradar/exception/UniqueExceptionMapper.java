package ru.hh.techradar.exception;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import ru.hh.techradar.enumeration.ExceptionType;

public class UniqueExceptionMapper implements ExceptionMapper<UniqueException> {

  @Override
  public Response toResponse(UniqueException exception) {
    return Response.status(Response.Status.BAD_REQUEST).
        entity(new Error(exception.getMessage(), Response.Status.BAD_REQUEST, ExceptionType.UNIQUE)).
        type(MediaType.APPLICATION_JSON).
        build();
  }
}
