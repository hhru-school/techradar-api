package ru.hh.techradar.exception;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import java.time.format.DateTimeParseException;

public class DateParseExceptionMapper implements ExceptionMapper<DateTimeParseException> {

  @Override
  public Response toResponse(DateTimeParseException exception) {
    return Response
        .status(Response.Status.BAD_REQUEST)
        .entity(new Error(exception.getMessage(), Response.Status.BAD_REQUEST))
        .type(MediaType.APPLICATION_JSON)
        .build();
  }
}
