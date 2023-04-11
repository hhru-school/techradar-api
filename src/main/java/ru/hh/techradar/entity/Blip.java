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
import java.util.List;
import java.util.Objects;

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

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "radar_id", nullable = false)
  private Radar radar;

  @OneToMany(mappedBy = "blip", fetch = FetchType.LAZY)
  private List<BlipEvent> logs;

  public Blip() {
  }

  public Blip(String name, String description, Radar radar) {
    this.name = name;
    this.description = description;
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

  public Radar getRadar() {
    return radar;
  }

  public void setRadar(Radar radar) {
    this.radar = radar;
  }

  public List<BlipEvent> getLogs() {
    return logs;
  }

  public void setLogs(List<BlipEvent> logs) {
    this.logs = logs;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Blip blip = (Blip) o;
    return Objects.equals(id, blip.id)
        && Objects.equals(name, blip.name)
        && Objects.equals(description, blip.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, description);
  }
}
