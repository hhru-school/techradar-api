package ru.hh.techradar.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.StringJoiner;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BlipDto {
  private Long id;
  private String name;
  private String description;
  private Long quadrantId;
  private Long ringId;
  private Long radarId;

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

  public Long getRadarId() {
    return radarId;
  }

  public void setRadarId(Long radarId) {
    this.radarId = radarId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BlipDto blipDto = (BlipDto) o;
    return id != null && id.equals(blipDto.id);
  }

  @Override
  public int hashCode() {
    return 11;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", BlipDto.class.getSimpleName() + "[", "]")
        .add("id=" + id)
        .add("name='" + name + "'")
        .add("description='" + description + "'")
        .add("quadrantId=" + quadrantId)
        .add("ringId=" + ringId)
        .toString();
  }
}
