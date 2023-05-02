package ru.hh.techradar.exception;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

public class EntityExistsExceptionMapper implements ExceptionMapper<EntityExistsException> {

  @Override
  public Response toResponse(EntityExistsException exception) {
    return Response.status(Response.Status.BAD_REQUEST).
        entity(new Error(exception.getMessage(), Response.Status.BAD_REQUEST)).
        type(MediaType.APPLICATION_JSON).
        build();
  }
}