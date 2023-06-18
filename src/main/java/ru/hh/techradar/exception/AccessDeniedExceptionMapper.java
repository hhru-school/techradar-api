package ru.hh.techradar.exception;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import org.springframework.security.access.AccessDeniedException;
import ru.hh.techradar.enumeration.ExceptionType;

public class AccessDeniedExceptionMapper implements ExceptionMapper<AccessDeniedException> {

  @Override
  public Response toResponse(AccessDeniedException exception) {
    return Response
        .status(Response.Status.FORBIDDEN)
        .entity(new Error(exception.getMessage(), Response.Status.FORBIDDEN, ExceptionType.ACCESS_DENIED))
        .type(MediaType.APPLICATION_JSON)
        .build();
  }
}
