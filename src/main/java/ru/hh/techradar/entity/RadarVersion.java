package ru.hh.techradar.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "radar_version")
public class RadarVersion extends AuditableEntity<Long> {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "radar_version_id", nullable = false)
  private Long id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "release", nullable = false)
  private Boolean release;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "radar_id", nullable = false)
  private Radar radar;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "blip_event_id", nullable = false)
  private BlipEvent blipEvent;

  public RadarVersion() {
  }

  public RadarVersion(
      Instant creationTime,
      Instant lastChangeTime,
      Long id,
      String name,
      Boolean release,
      Radar radar,
      BlipEvent blipEvent) {
    super(creationTime, lastChangeTime);
    this.id = id;
    this.name = name;
    this.release = release;
    this.radar = radar;
    this.blipEvent = blipEvent;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Boolean getRelease() {
    return release;
  }

  public void setRelease(Boolean release) {
    this.release = release;
  }

  public Radar getRadar() {
    return radar;
  }

  public void setRadar(Radar radar) {
    this.radar = radar;
  }

  public BlipEvent getBlipEvent() {
    return blipEvent;
  }

  public void setBlipEvent(BlipEvent blipEvent) {
    this.blipEvent = blipEvent;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof RadarVersion that)) {
      return false;
    }
    return name.equals(that.name) && release.equals(that.release) && radar.equals(that.radar) && Objects.equals(blipEvent, that.blipEvent);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, release, radar, blipEvent);
  }
}
