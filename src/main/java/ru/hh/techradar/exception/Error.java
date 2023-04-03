package ru.hh.techradar.exception;

import jakarta.ws.rs.core.Response;
import java.time.ZonedDateTime;

public class Error {
  private String message;
  private Response.Status status;
  private ZonedDateTime timestamp = ZonedDateTime.now();

  public Error() {
  }

  public Error(String message, Response.Status status) {
    this.message = message;
    this.status = status;
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

  public ZonedDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(ZonedDateTime timestamp) {
    this.timestamp = timestamp;
  }

}
