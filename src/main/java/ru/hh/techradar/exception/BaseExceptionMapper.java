package ru.hh.techradar.exception;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import ru.hh.techradar.enumeration.ExceptionType;

public class BaseExceptionMapper implements ExceptionMapper<Exception> {

  @Override
  public Response toResponse(Exception exception) {
    return Response
        .status(Response.Status.BAD_REQUEST)
        .entity(new Error(exception.getMessage(), Response.Status.BAD_REQUEST, ExceptionType.BASE))
        .type(MediaType.APPLICATION_JSON)
        .build();
  }
}
