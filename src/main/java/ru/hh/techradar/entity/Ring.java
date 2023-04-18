package ru.hh.techradar.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
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

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "radar_id", nullable = false)
  private Radar radar;

  @OneToMany(mappedBy = "ring", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private List<RingSetting> settings = new ArrayList<>();

  public Ring() {
  }

  public Ring(Radar radar, Instant creationTime) {
    this.radar = radar;
    setCreationTime(creationTime);
    setLastChangeTime(creationTime);
  }

  public Ring(Long id, Radar radar, Instant creationTime, Instant lastChangeTime, Instant removedAt) {
    this.id = id;
    this.radar = radar;
    setCreationTime(creationTime);
    setLastChangeTime(lastChangeTime);
    this.removedAt = removedAt;
  }

  public List<RingSetting> getSettings() {
    return settings;
  }

  public void setSettings(List<RingSetting> settings) {
    this.settings = settings;
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

  public Radar getRadar() {
    return radar;
  }

  public void setRadar(Radar radar) {
    this.radar = radar;
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
        ", removedAt=" + removedAt +
        ", radarId=" + radar.getId() +
        ", creationTime=" + getCreationTime() +
        ", lastChangeTime=" + getLastChangeTime()
        + '\n' + '}';
  }
}
