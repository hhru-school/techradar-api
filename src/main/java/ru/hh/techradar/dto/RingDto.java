package ru.hh.techradar.dto;

import java.util.Objects;

public class RingDto {
  private Long id;
  private String name;
  private Integer position;

  public RingDto() {
  }

  public RingDto(Long id, String name, Integer position) {
    this.id = id;
    this.name = name;
    this.position = position;
  }

  public Integer getPosition() {
    return position;
  }

  public void setPosition(Integer position) {
    this.position = position;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RingDto ringDto = (RingDto) o;
    return Objects.equals(id, ringDto.id)
        && Objects.equals(name, ringDto.name)
        && Objects.equals(position, ringDto.position);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, position);
  }

  @Override
  public String toString() {
    return "RingDto{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", position=" + position +
        '}';
  }
}
