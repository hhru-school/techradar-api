package ru.hh.techradar.entity.exception;

import jakarta.ws.rs.core.Response;
import java.time.Instant;

public class Error {
  private String message;
  private Response.Status status;
  private Instant timestamp;

  public Error() {
  }

  public Error(String message, Response.Status status) {
    this.message = message;
    this.status = status;
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
}
