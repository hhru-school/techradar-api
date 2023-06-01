package ru.hh.techradar.filter;

import jakarta.ws.rs.QueryParam;

public class RevisionFilter {
  @QueryParam("blip-event-id")
  private Long blipEventId;

  @QueryParam("radar-version-id")
  private Long radarVersionId;

  public RevisionFilter() {
  }

  public RevisionFilter(Long blipEventId, Long radarVersionId) {
    this.blipEventId = blipEventId;
    this.radarVersionId = radarVersionId;
  }

  public Long getBlipEventId() {
    return blipEventId;
  }

  public void setBlipEventId(Long blipEventId) {
    this.blipEventId = blipEventId;
  }

  public Long getRadarVersionId() {
    return radarVersionId;
  }

  public void setRadarVersionId(Long radarVersionId) {
    this.radarVersionId = radarVersionId;
  }
}
