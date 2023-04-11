package ru.hh.techradar.entity;

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
import java.util.List;


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

  @OneToMany(mappedBy = "quadrant", fetch = FetchType.LAZY)
  private List<QuadrantSetting> settings;

  public Quadrant() {
  }

  public Quadrant(Radar radar) {
    this.radar = radar;
  }
}
