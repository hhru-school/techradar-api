package ru.hh.techradar.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BlipCreateDto {
  private Long id;
  private String name;
  private String description;
  private QuadrantDto quadrant;
  private RingDto ring;
  private RadarDto radar;
  private String comment;

  public BlipCreateDto() {
  }

  public BlipCreateDto(Long id, String name, String description, QuadrantDto quadrant, RingDto ring, RadarDto radar, String comment) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.quadrant = quadrant;
    this.ring = ring;
    this.radar = radar;
    this.comment = comment;
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

  public RadarDto getRadar() {
    return radar;
  }

  public void setRadar(RadarDto radar) {
    this.radar = radar;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }
}
