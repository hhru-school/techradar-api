package ru.hh.techradar.exception;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import ru.hh.techradar.enumeration.ExceptionType;

public class OperationNotAllowedExceptionMapper implements ExceptionMapper<OperationNotAllowedException> {
  @Override
  public Response toResponse(OperationNotAllowedException exception) {
    return Response
        .status(Response.Status.BAD_REQUEST)
        .entity(new Error(exception.getMessage(), Response.Status.BAD_REQUEST, ExceptionType.OPERATION_NOT_ALLOWED))
        .type(MediaType.APPLICATION_JSON)
        .build();
  }
}
