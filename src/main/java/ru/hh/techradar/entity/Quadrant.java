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
@Table(name = "quadrant")
public class Quadrant extends AuditableEntity<Long> {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "quadrant_id", nullable = false)
  private Long id;

  @Column(name = "name", nullable = false)
  private String name;

  @ManyToOne
  @JoinColumn(name = "radar_id", nullable = false)
  private Radar radar;

  @Column(name = "position", nullable = false)
  private Integer position;

  @Column(name = "removed_at")
  private Instant removedAt;

  public Quadrant() {}

  public Quadrant(Long id, String name, Radar radar, Integer position, Instant removedAt, Instant creationTime, Instant lastChangeTime) {
    this.id = id;
    this.name = name;
    this.radar = radar;
    this.position = position;
    this.removedAt = removedAt;
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
    Quadrant quadrant = (Quadrant) o;
    return id.equals(quadrant.id)
        && name.equals(quadrant.name)
        && radar.equals(quadrant.radar)
        && position.equals(quadrant.position)
        && removedAt.equals(quadrant.removedAt)
        && getCreationTime().equals(quadrant.getCreationTime())
        && getLastChangeTime().equals(quadrant.getLastChangeTime());
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, radar, position, removedAt, getCreationTime(), getLastChangeTime());
  }

  @Override
  public String toString() {
    return "Ring {" +
        "id=" + id +
        ", name=" + name +
        ", radar=" + radar +
        ", position=" + position +
        ", removedAt=" + removedAt +
        ", creationTime=" + getCreationTime() +
        ", lastChangeTime=" + getLastChangeTime()
        + '\n' + '}';
  }
}
