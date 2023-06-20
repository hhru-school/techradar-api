package ru.hh.techradar.dto;
import java.time.Instant;
import java.util.Objects;

public class BlipEventShortReadDto {
  private Long id;
  private String comment;
  private Long parentId;
  private Long blipId;
  private Long quadrantId;
  private Long ringId;
  private Long authorId;
  private Long radarId;
  private Instant creationTime;
  private Instant lastChangeTime;

  public BlipEventShortReadDto() {
  }

  public BlipEventShortReadDto(
      Long id,
      String comment,
      Long parentId, Long blipId,
      Long quadrantId,
      Long ringId,
      Long authorId,
      Long radarId,
      Instant creationTime,
      Instant lastChangeTime
  ) {
    this.id = id;
    this.comment = comment;
    this.parentId = parentId;
    this.blipId = blipId;
    this.quadrantId = quadrantId;
    this.ringId = ringId;
    this.authorId = authorId;
    this.radarId = radarId;
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

  public Long getBlipId() {
    return blipId;
  }

  public void setBlipId(Long blipId) {
    this.blipId = blipId;
  }

  public Long getQuadrantId() {
    return quadrantId;
  }

  public void setQuadrantId(Long quadrantId) {
    this.quadrantId = quadrantId;
  }

  public Long getRingId() {
    return ringId;
  }

  public void setRingId(Long ringId) {
    this.ringId = ringId;
  }

  public Long getAuthorId() {
    return authorId;
  }

  public void setAuthorId(Long authorId) {
    this.authorId = authorId;
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

  public Long getParentId() {
    return parentId;
  }

  public void setParentId(Long parentId) {
    this.parentId = parentId;
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
    if (!(o instanceof BlipEventShortReadDto that)) {
      return false;
    }
    return Objects.equals(comment, that.comment) && Objects.equals(parentId, that.parentId) && Objects.equals(
        blipId,
        that.blipId
    ) && Objects.equals(quadrantId, that.quadrantId) && Objects.equals(
        ringId,
        that.ringId
    ) && authorId.equals(that.authorId) && radarId.equals(that.radarId);
  }

  @Override
  public int hashCode() {
    return 11;
  }

  @Override
  public String toString() {
    return "BlipEventDto{" +
        "id=" + id +
        ", comment='" + comment + '\'' +
        ", blipId=" + blipId +
        ", quadrantId=" + quadrantId +
        ", ringId=" + ringId +
        ", authorId=" + authorId +
        ", creationTime=" + creationTime +
        ", lastChangeTime=" + lastChangeTime +
        '}';
  }
}
