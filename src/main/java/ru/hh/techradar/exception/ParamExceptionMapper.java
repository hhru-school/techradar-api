package ru.hh.techradar.exception;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import java.util.Optional;
import org.glassfish.jersey.server.ParamException;

public class ParamExceptionMapper implements ExceptionMapper<ParamException> {

  @Override
  public Response toResponse(ParamException exception) {
    String message = Optional
        .ofNullable(exception.getCause())
        .map(Throwable::getMessage)
        .orElse(exception.getMessage());
    return Response
        .status(Response.Status.BAD_REQUEST)
        .entity(new Error(message, Response.Status.BAD_REQUEST))
        .type(MediaType.APPLICATION_JSON)
        .build();
  }
}
