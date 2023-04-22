package ru.hh.techradar.dto;

import jakarta.validation.constraints.NotNull;
import java.util.Objects;

public class BlipDto {
  @NotNull
  private Long id;
  @NotNull
  private String name;
  private String description;
  private Long quadrantId;
  private Long ringId;

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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof BlipDto blipDto)) {
      return false;
    }
    return Objects.equals(id, blipDto.id) && name.equals(blipDto.name) && Objects.equals(
        description,
        blipDto.description
    ) && quadrantId.equals(blipDto.quadrantId) && ringId.equals(blipDto.ringId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, description, quadrantId, ringId);
  }
}
