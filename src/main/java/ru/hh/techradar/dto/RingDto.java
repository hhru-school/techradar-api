package ru.hh.techradar.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public class RingDto {
  @NotNull
  private Long id;
  @NotNull
  private String name;
  @NotNull
  @DecimalMax(message = "Position must be less than 8", value = "8")
  @DecimalMin(message = "Position must be bigger than 0", value = "1")
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
    return id != null && id.equals(ringDto.id);
  }

  @Override
  public int hashCode() {
    return 11;
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
