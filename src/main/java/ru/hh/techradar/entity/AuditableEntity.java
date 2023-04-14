package ru.hh.techradar.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.Instant;

@MappedSuperclass
public abstract class AuditableEntity<T extends Serializable> {
  @Column(name = "creation_time", nullable = false)
  private Instant creationTime = Instant.now();
  @Column(name = "last_change_time", nullable = false)
  private Instant lastChangeTime = Instant.now();

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
