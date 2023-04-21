package ru.hh.techradar.dto;

import jakarta.validation.constraints.NotNull;
import java.util.Objects;

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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof BlipDto blipDto)) {
      return false;
    }

    if (!id.equals(blipDto.id)) {
      return false;
    }
    if (!name.equals(blipDto.name)) {
      return false;
    }
    if (!Objects.equals(description, blipDto.description)) {
      return false;
    }
    if (!Objects.equals(quadrant, blipDto.quadrant)) {
      return false;
    }
    return Objects.equals(ring, blipDto.ring);
  }

  @Override
  public int hashCode() {
    int result = id.hashCode();
    result = 31 * result + name.hashCode();
    result = 31 * result + (description != null ? description.hashCode() : 0);
    result = 31 * result + (quadrant != null ? quadrant.hashCode() : 0);
    result = 31 * result + (ring != null ? ring.hashCode() : 0);
    return result;
  }
}
