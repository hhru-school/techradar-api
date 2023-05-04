package ru.hh.techradar.dto;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public class BlipEventDto {
  private Long id;
  private String comment;
  private String versionName;
  @NotNull
  private Long blipId;
  @NotNull
  private Long quadrantId;
  @NotNull
  private Long ringId;
  @NotNull
  private Long authorId;
  private Instant creationTime;
  private Instant lastChangeTime;

  public BlipEventDto() {
  }

  public BlipEventDto(
      Long id,
      String comment,
      String versionName,
      Long blipId,
      Long quadrantId,
      Long ringId,
      Long authorId,
      Instant creationTime,
      Instant lastChangeTime
  ) {
    this.id = id;
    this.comment = comment;
    this.versionName = versionName;
    this.blipId = blipId;
    this.quadrantId = quadrantId;
    this.ringId = ringId;
    this.authorId = authorId;
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

  public String getVersionName() {
    return versionName;
  }

  public void setVersionName(String versionName) {
    this.versionName = versionName;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BlipEventDto blipEventDto = (BlipEventDto) o;
    return id != null && id.equals(blipEventDto.id);
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
        ", versionName='" + versionName + '\'' +
        ", blipId=" + blipId +
        ", quadrantId=" + quadrantId +
        ", ringId=" + ringId +
        ", authorId=" + authorId +
        ", creationTime=" + creationTime +
        ", lastChangeTime=" + lastChangeTime +
        '}';
  }
}
