package ru.hh.techradar.exception;

import jakarta.ws.rs.core.Response;
import java.time.Instant;
import ru.hh.techradar.enumeration.ExceptionType;

public class Error {
  private String message;
  private Response.Status status;
  private Instant timestamp;

  private ExceptionType type;

  public Error() {
  }

  public Error(
      String message,
      Response.Status status,
      ExceptionType type) {
    this.message = message;
    this.status = status;
    this.type = type;
    this.timestamp = Instant.now();
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Response.Status getStatus() {
    return status;
  }

  public void setStatus(Response.Status status) {
    this.status = status;
  }

  public Instant getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Instant timestamp) {
    this.timestamp = timestamp;
  }

  public ExceptionType getType() {
    return type;
  }

  public void setType(ExceptionType type) {
    this.type = type;
  }
}
