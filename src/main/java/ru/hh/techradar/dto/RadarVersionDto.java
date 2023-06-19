package ru.hh.techradar.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Instant;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RadarVersionDto {
  private Long id;
  private String name;
  private Boolean release;
  private Long radarId;
  private Long blipEventId;
  private Long parentId;
  private String parentName;
  private Integer level;
  private Boolean toggleAvailable;
  private Instant creationTime;
  private Instant lastChangeTime;

  public RadarVersionDto() {
  }

  public RadarVersionDto(Long id, String name, Boolean release, Long radarId, Long blipEventId,
      Long parentId, String parentName, Integer level,
      Boolean toggleAvailable, Instant creationTime, Instant lastChangeTime) {
    this.id = id;
    this.name = name;
    this.release = release;
    this.radarId = radarId;
    this.blipEventId = blipEventId;
    this.parentId = parentId;
    this.parentName = parentName;
    this.level = level;
    this.toggleAvailable = toggleAvailable;
    this.creationTime = creationTime;
    this.lastChangeTime = lastChangeTime;
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

  public Boolean getRelease() {
    return release;
  }

  public void setRelease(Boolean release) {
    this.release = release;
  }

  public Long getRadarId() {
    return radarId;
  }

  public void setRadarId(Long radarId) {
    this.radarId = radarId;
  }

  public Long getBlipEventId() {
    return blipEventId;
  }

  public void setBlipEventId(Long blipEventId) {
    this.blipEventId = blipEventId;
  }

  public Long getParentId() {
    return parentId;
  }

  public void setParentId(Long parentId) {
    this.parentId = parentId;
  }

  public String getParentName() {
    return parentName;
  }

  public void setParentName(String parentName) {
    this.parentName = parentName;
  }

  public Integer getLevel() {
    return level;
  }

  public void setLevel(Integer level) {
    this.level = level;
  }

  public Boolean getToggleAvailable() {
    return toggleAvailable;
  }

  public void setToggleAvailable(Boolean toggleAvailable) {
    this.toggleAvailable = toggleAvailable;
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
    if (!(o instanceof RadarVersionDto that)) {
      return false;
    }
    return name.equals(that.name) && radarId.equals(that.radarId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, radarId);
  }
}
