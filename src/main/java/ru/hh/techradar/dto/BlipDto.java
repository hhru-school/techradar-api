package ru.hh.techradar.dto;

import jakarta.validation.constraints.NotNull;

public class BlipDto {
  @NotNull
  private Long id;
  @NotNull
  private String name;
  private String description;
  private Long quadrantId;
  private Long ringId;
  private Long radarId;

  public BlipDto() {
  }

  public BlipDto(Long id, String name, String description, Long radarId) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.radarId = radarId;
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
