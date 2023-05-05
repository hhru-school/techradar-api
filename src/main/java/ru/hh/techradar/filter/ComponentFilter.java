package ru.hh.techradar.filter;

import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.QueryParam;
import java.time.Instant;

public class ComponentFilter {
  @QueryParam("radarId")
  @NotNull(message = "Cannot be null property radarId")
  private Long radarId;

  @QueryParam(value = "actualDate")
  @DefaultValue(value = "9999-01-01T00:00:00.000Z")
  private Instant actualDate;

  public ComponentFilter() {
  }

  public ComponentFilter radarId(Long radarId) {
    this.setRadarId(radarId);
    return this;
  }

  public ComponentFilter actualDate(Instant actualDate) {
    this.setActualDate(actualDate);
    return this;
  }

  public Long getRadarId() {
    return radarId;
  }

  public void setRadarId(Long radarId) {
    this.radarId = radarId;
  }

  public Instant getActualDate() {
    return actualDate;
  }

  public void setActualDate(Instant actualDate) {
    this.actualDate = actualDate;
  }
}
