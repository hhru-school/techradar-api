package ru.hh.techradar.dto;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import ru.hh.techradar.entity.Radar;
import ru.hh.techradar.entity.RingSetting;

public class RingDto {
  private Long id;
  private String name;
  private Integer position;
  private Instant removedAt;
  private Instant creationTime;
  private Instant lastChangeTime;

  public RingDto() {}

  public RingDto(Long id, String name, Integer position, Instant creationTime, Instant lastChangeTime, Instant removedAt) {
    this.id = id;
    this.removedAt = removedAt;
    this.name = name;
    this.position = position;
    this.creationTime = creationTime;
    this.lastChangeTime = lastChangeTime;
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

  public Instant getRemovedAt() {
    return removedAt;
  }

  public void setRemovedAt(Instant removedAt) {
    this.removedAt = removedAt;
  }

  public Instant getCreationTime() {
    return creationTime;
  }

  public void setCreationTime(Instant creationTime) {
    this.creationTime = creationTime;
  }

  public Instant getLastChangeTime() {
    return lastChangeTime;
  }

  public void setLastChangeTime(Instant lastChangeTime) {
    this.lastChangeTime = lastChangeTime;
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
    return Objects.equals(id, ringDto.id) && Objects.equals(name, ringDto.name) && Objects.equals(
        position,
        ringDto.position
    ) && Objects.equals(removedAt, ringDto.removedAt) && Objects.equals(
        creationTime,
        ringDto.creationTime
    ) && Objects.equals(lastChangeTime, ringDto.lastChangeTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, position, removedAt, creationTime, lastChangeTime);
  }

  @Override
  public String toString() {
    return "RingDto{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", position=" + position +
        ", removedAt=" + removedAt +
        ", creationTime=" + creationTime +
        ", lastChangeTime=" + lastChangeTime +
        '}';
  }
}
