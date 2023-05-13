package ru.hh.techradar.exception;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

public class BaseExceptionMapper implements jakarta.ws.rs.ext.ExceptionMapper<Exception> {

  @Override
  public Response toResponse(Exception exception) {
    return Response
        .status(Response.Status.BAD_REQUEST)
        .entity(new Error(exception.getMessage(), Response.Status.BAD_REQUEST))
        .type(MediaType.APPLICATION_JSON)
        .build();
  }
}
