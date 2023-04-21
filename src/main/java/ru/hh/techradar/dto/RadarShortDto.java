package ru.hh.techradar.dto;

import java.util.Objects;

public class RadarShortDto {
  private Long id;
  private String name;

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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof RadarShortDto that)) {
      return false;
    }
    return id.equals(that.id) && name.equals(that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name);
  }

  @Override
  public String toString() {
    return "RadarReadDto{" +
        "id=" + id +
        ", name='" + name + '\'' +
        '}';
  }
}
