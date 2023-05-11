package ru.hh.techradar.dto;

public class RingDto {
  private Long id;
  private String name;
  private Integer position;
  private Long radarId;

  public RingDto() {
  }

  public RingDto(Long id, String name, Integer position, Long radarId) {
    this.id = id;
    this.name = name;
    this.position = position;
    this.radarId = radarId;
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
