package ru.hh.techradar.exception;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {

  @Override
  public Response toResponse(NotFoundException exception) {
    return Response.status(Response.Status.NOT_FOUND).
        entity(new Error(exception.getMessage(), Response.Status.NOT_FOUND)).
        type(MediaType.APPLICATION_JSON).
        build();
  }
}
