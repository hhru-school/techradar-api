package ru.hh.techradar.exception;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import org.springframework.dao.DataIntegrityViolationException;

public class HibernateDataIntegrityViolationExceptionMapper implements ExceptionMapper<DataIntegrityViolationException> {
  @Override
  public Response toResponse(DataIntegrityViolationException exception) {
    String message = exception.getLocalizedMessage();
    return Response
        .status(Response.Status.BAD_REQUEST)
        .entity(new Error(message, Response.Status.BAD_REQUEST))
        .type(MediaType.APPLICATION_JSON)
        .build();
  }
}
