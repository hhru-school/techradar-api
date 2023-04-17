package ru.hh.techradar.dto;

import jakarta.validation.constraints.NotNull;

public class BlipDto {
  @NotNull
  private Long id;
  @NotNull
  private String name;
  private String description;

  private QuadrantDto quadrant;
  private RingDto ring;

  public BlipDto() {
  }

  public BlipDto(Long id, String name, String description, BlipEventDto blipEventDto) {
    this.id = id;
    this.name = name;
    this.description = description;
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

  public QuadrantDto getQuadrant() {
    return quadrant;
  }

  public void setQuadrant(QuadrantDto quadrant) {
    this.quadrant = quadrant;
  }

  public RingDto getRing() {
    return ring;
  }

  public void setRing(RingDto ring) {
    this.ring = ring;
  }
}
