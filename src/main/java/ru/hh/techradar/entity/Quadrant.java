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
import java.util.Comparator;
import java.util.List;
import java.util.StringJoiner;

@Entity
@Table(name = "quadrant")
public class Quadrant extends AuditableEntity<Long> {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "quadrant_id", nullable = false)
  private Long id;

  @Column(name = "removed_at")
  private Instant removedAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "radar_id", nullable = false)
  private Radar radar;

  @OneToMany(mappedBy = "quadrant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<QuadrantSetting> settings = new ArrayList<>();

  public Quadrant() {
  }

  public Quadrant(Radar radar) {
    this.radar = radar;
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

  public List<QuadrantSetting> getSettings() {
    return settings;
  }

  public void setSettings(List<QuadrantSetting> settings) {
    this.settings = settings;
  }

  public QuadrantSetting getCurrentSetting() {
    return settings.stream()
        .sorted(Comparator.comparing(QuadrantSetting::getId).reversed())
        .toList().get(0);
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
    return id != null && id.equals(quadrant.id);
  }

  @Override
  public int hashCode() {
    return 11;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", Quadrant.class.getSimpleName() + "[", "]")
        .add("id=" + id)
        .add("removedAt=" + removedAt)
        .toString();
  }
}
