package ru.hh.techradar.dto;

import java.util.Objects;

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
    QuadrantDto that = (QuadrantDto) o;
    return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(position, that.position);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, position);
  }
}
