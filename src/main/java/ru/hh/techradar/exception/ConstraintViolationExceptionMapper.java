package ru.hh.techradar.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import java.util.stream.Collectors;

public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {


  @Override
  public Response toResponse(ConstraintViolationException exception) {
    String message = exception.getConstraintViolations().stream()
        .map(ConstraintViolation::getMessage)
        .collect(Collectors.joining("; ", "", ";"));
    return Response
        .status(Response.Status.BAD_REQUEST)
        .entity(new Error(message, Response.Status.BAD_REQUEST))
        .type(MediaType.APPLICATION_JSON)
        .build();
  }
}
