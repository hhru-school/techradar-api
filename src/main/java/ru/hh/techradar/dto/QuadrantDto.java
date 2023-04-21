package ru.hh.techradar.dto;

import java.util.Objects;

public class QuadrantDto {
  private Long id;
  private String name;
  private Integer position;
  private Long radarId;

  public QuadrantDto() {
  }

  public QuadrantDto(Long id, String name, Integer position, Long radarId) {
    this.id = id;
    this.name = name;
    this.position = position;
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

  public Integer getPosition() {
    return position;
  }

  public void setPosition(Integer position) {
    this.position = position;
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
    if (!(o instanceof QuadrantDto that)) {
      return false;
    }

    if (!Objects.equals(id, that.id)) {
      return false;
    }
    if (!name.equals(that.name)) {
      return false;
    }
    if (!position.equals(that.position)) {
      return false;
    }
    return radarId.equals(that.radarId);
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + name.hashCode();
    result = 31 * result + position.hashCode();
    result = 31 * result + radarId.hashCode();
    return result;
  }
}
