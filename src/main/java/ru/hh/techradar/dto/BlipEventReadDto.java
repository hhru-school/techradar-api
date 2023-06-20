package ru.hh.techradar.dto;

import java.time.Instant;
import ru.hh.techradar.enumeration.DrawInfoType;

public class BlipEventReadDto {
  private Long id;
  private String comment;
  private Long parentId;
  private BlipDto blip;
  private QuadrantDto quadrant;
  private RingDto ring;
  private DrawInfoType drawInfo;
  private String radarVersion;
  private UserDto author;
  private Instant creationTime;
  private Instant lastChangeTime;

  public BlipEventReadDto() {
  }

  public BlipEventReadDto(
      Long id,
      String comment,
      Long parentId,
      BlipDto blip,
      QuadrantDto quadrant,
      RingDto ring,
      DrawInfoType drawInfo,
      String radarVersion,
      UserDto author,
      Instant creationTime,
      Instant lastChangeTime) {
    this.id = id;
    this.comment = comment;
    this.parentId = parentId;
    this.blip = blip;
    this.quadrant = quadrant;
    this.ring = ring;
    this.drawInfo = drawInfo;
    this.radarVersion = radarVersion;
    this.author = author;
    this.creationTime = creationTime;
    this.lastChangeTime = lastChangeTime;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public Long getParentId() {
    return parentId;
  }

  public void setParentId(Long parentId) {
    this.parentId = parentId;
  }

  public BlipDto getBlip() {
    return blip;
  }

  public void setBlip(BlipDto blip) {
    this.blip = blip;
  }

  public QuadrantDto getQuadrant() {
    return quadrant;
  }

  public void setQuadrant(QuadrantDto quadrant) {
    this.quadrant = quadrant;
  }

  public RingDto getRing() {
    return ring;
  }

  public void setRing(RingDto ring) {
    this.ring = ring;
  }

  public UserDto getAuthor() {
    return author;
  }

  public void setAuthor(UserDto author) {
    this.author = author;
  }

  public DrawInfoType getDrawInfo() {
    return drawInfo;
  }

  public void setDrawInfo(DrawInfoType drawInfo) {
    this.drawInfo = drawInfo;
  }

  public String getRadarVersion() {
    return radarVersion;
  }

  public void setRadarVersion(String radarVersion) {
    this.radarVersion = radarVersion;
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
}
