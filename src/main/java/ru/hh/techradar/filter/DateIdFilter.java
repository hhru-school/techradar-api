package ru.hh.techradar.filter;

import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import java.time.Instant;

public class DateIdFilter {
  @PathParam("id")
  @NotNull(message = "Cannot be null property id")
  private Long id;

  @QueryParam(value = "date")
  @DefaultValue(value = "9999-01-01T00:00:00.000Z")
  private Instant date;

  public DateIdFilter(Long id, Instant date) {
    this.id = id;
    this.date = date;
  }

  public DateIdFilter id(Long id) {
    this.id = id;
    return this;
  }

  public DateIdFilter actualDate(Instant actualDate) {
    this.setDate(actualDate);
    return this;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Instant getDate() {
    return date;
  }

  public void setDate(Instant date) {
    this.date = date;
  }

}
