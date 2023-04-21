package ru.hh.techradar.dto;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Objects;

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
    if (!(o instanceof BlipEventDto that)) {
      return false;
    }

    if (!Objects.equals(id, that.id)) {
      return false;
    }
    if (!Objects.equals(comment, that.comment)) {
      return false;
    }
    if (!Objects.equals(versionName, that.versionName)) {
      return false;
    }
    if (!blipId.equals(that.blipId)) {
      return false;
    }
    if (!quadrantId.equals(that.quadrantId)) {
      return false;
    }
    if (!ringId.equals(that.ringId)) {
      return false;
    }
    if (!authorId.equals(that.authorId)) {
      return false;
    }
    if (!Objects.equals(creationTime, that.creationTime)) {
      return false;
    }
    return Objects.equals(lastChangeTime, that.lastChangeTime);
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (comment != null ? comment.hashCode() : 0);
    result = 31 * result + (versionName != null ? versionName.hashCode() : 0);
    result = 31 * result + blipId.hashCode();
    result = 31 * result + quadrantId.hashCode();
    result = 31 * result + ringId.hashCode();
    result = 31 * result + authorId.hashCode();
    result = 31 * result + (creationTime != null ? creationTime.hashCode() : 0);
    result = 31 * result + (lastChangeTime != null ? lastChangeTime.hashCode() : 0);
    return result;
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
