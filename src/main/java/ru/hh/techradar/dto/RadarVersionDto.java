package ru.hh.techradar.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RadarVersionDto {
  private Long id;
  private String name;
  private Boolean release;
  private Long radarId;
  private Long blipEventId;

  public RadarVersionDto() {
  }

  public RadarVersionDto(Long id, String name, Boolean release, Long radarId, Long blipEventId) {
    this.id = id;
    this.name = name;
    this.release = release;
    this.radarId = radarId;
    this.blipEventId = blipEventId;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Boolean getRelease() {
    return release;
  }

  public void setRelease(Boolean release) {
    this.release = release;
  }

  public Long getRadarId() {
    return radarId;
  }

  public void setRadarId(Long radarId) {
    this.radarId = radarId;
  }

  public Long getBlipEventId() {
    return blipEventId;
  }

  public void setBlipEventId(Long blipEventId) {
    this.blipEventId = blipEventId;
  }
}
