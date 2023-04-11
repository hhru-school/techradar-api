package ru.hh.techradar.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "ring")
public class Ring extends AuditableEntity<Long> {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ring_id", nullable = false)
  private Long id;

  @Column(name = "removed_at")
  private Instant removedAt;

  @ManyToOne
  @JoinColumn(name = "radar_id", nullable = false)
  private Radar radar;
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

  public Radar getRadar() {
    return radarId;
  }

  public void setRadar(Radar radar) {
    this.radar = radar;
  }

  public Ring() {}

  public Ring(Long id, Instant removedAt, Radar radar, Instant creationTime, Instant lastChangeTime) {
    this.id = id;
    this.removedAt = removedAt;
    this.radar = radar;
    setCreationTime(creationTime);
    setLastChangeTime(lastChangeTime);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Ring ring = (Ring) o;
    return id.equals(ring.id)
        && removedAt.equals(ring.removedAt)
        && radar.equals(ring.radar)
        && getCreationTime().equals(ring.getCreationTime())
        && getLastChangeTime().equals(ring.getLastChangeTime());
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, removedAt, radar, getCreationTime(), getLastChangeTime());
  }

  @Override
  public String toString() {
    return "Ring {" +
        "id=" + id +
        ", removedAt='" + removedAt +
        ", radarId" + radar +
        ", creationTime" + getCreationTime() +
        ", lastChangeTime" + getLastChangeTime()
        + '\n' + '}';
  }
}
