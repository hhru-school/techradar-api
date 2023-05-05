package ru.hh.techradar.filter;

import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.QueryParam;
import java.time.Instant;

public class BlipFilter {
  @QueryParam("blipId")
  @NotNull(message = "Cannot be null property blipId")
  private Long blipId;

  @QueryParam(value = "actualDate")
  @DefaultValue(value = "9999-01-01T00:00:00.000Z")
  private Instant actualDate;

  public BlipFilter() {
  }

  public BlipFilter blipId(Long radarId) {
    this.setBlipId(radarId);
    return this;
  }

  public BlipFilter actualDate(Instant actualDate) {
    this.setActualDate(actualDate);
    return this;
  }

  public Long getBlipId() {
    return blipId;
  }

  public void setBlipId(Long blipId) {
    this.blipId = blipId;
  }

  public Instant getActualDate() {
    return actualDate;
  }

  public void setActualDate(Instant actualDate) {
    this.actualDate = actualDate;
  }
}
