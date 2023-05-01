package ru.hh.techradar.dto;

import jakarta.validation.constraints.NotNull;

public class BlipCreateDto {
  @NotNull
  private String name;
  private String description;
  @NotNull
  private Long quadrantId;
  @NotNull
  private Long ringId;
  @NotNull
  private Long radarId;

  public BlipCreateDto() {
  }

  public BlipCreateDto(String name, String description, Long quadrantId, Long ringId, Long radarId) {
    this.name = name;
    this.description = description;
    this.quadrantId = quadrantId;
    this.ringId = ringId;
    this.radarId = radarId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Long getQuadrantId() {
    return quadrantId;
  }

  public void setQuadrantId(Long quadrantId) {
    this.quadrantId = quadrantId;
  }

  public Long getRingId() {
    return ringId;
  }

  public void setRingId(Long ringId) {
    this.ringId = ringId;
  }

  public Long getRadarId() {
    return radarId;
  }

  public void setRadarId(Long radarId) {
    this.radarId = radarId;
  }
}
