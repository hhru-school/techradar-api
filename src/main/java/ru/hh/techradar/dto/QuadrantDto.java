package ru.hh.techradar.dto;

import java.util.Objects;
import java.util.StringJoiner;

public class QuadrantDto {
  private Long id;
  private String name;
  private Integer position;

  public QuadrantDto() {
  }

  public QuadrantDto(Long id, String name, Integer position) {
    this.id = id;
    this.name = name;
    this.position = position;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    QuadrantDto dto = (QuadrantDto) o;
    return Objects.equals(id, dto.id) && Objects.equals(name, dto.name) && Objects.equals(position, dto.position);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, position);
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", QuadrantDto.class.getSimpleName() + "[", "]")
        .add("id=" + id)
        .add("name='" + name + "'")
        .add("position=" + position)
        .toString();
  }
}
