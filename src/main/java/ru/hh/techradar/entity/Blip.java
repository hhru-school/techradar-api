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

@Entity
@Table(name = "blip")
public class Blip extends AuditableEntity<Long> {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "blip_id", nullable = false)
  private Long id;
  @Column(name = "name", nullable = false)
  private String name;
  @Column(name = "description")
  private String description;
  @ManyToOne
  @JoinColumn(name = "quadrant_id", nullable = false)
  private Quadrant quadrant;
  @ManyToOne
  @JoinColumn(name = "ring_id", nullable = false)
  private Ring ring;
  @ManyToOne
  @JoinColumn(name = "radar_id", nullable = false)
  private Radar radar;

  public Blip() {
  }

  public Blip(
      Instant creationTime,
      Instant lastChangeTime,
      Long id,
      String name,
      String description,
      Quadrant quadrant,
      Ring ring,
      Radar radar) {
    super(creationTime, lastChangeTime);
    this.id = id;
    this.name = name;
    this.description = description;
    this.quadrant = quadrant;
    this.ring = ring;
    this.radar = radar;
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

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Quadrant getQuadrant() {
    return quadrant;
  }

  public void setQuadrant(Quadrant quadrant) {
    this.quadrant = quadrant;
  }

  public Ring getRing() {
    return ring;
  }

  public void setRing(Ring ring) {
    this.ring = ring;
  }

  public Radar getRadar() {
    return radar;
  }

  public void setRadar(Radar radar) {
    this.radar = radar;
  }
}
