package ru.hh.techradar.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import ru.hh.techradar.dto.RingDto;

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
  @JoinColumn(name = "radar_id", nullable = false)
  private Radar radar;
  @OneToMany(mappedBy = "blip", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<BlipEvent> blipEvents = new ArrayList<>();

  public Blip() {
  }

  public Blip(Long id, String name, String description, Radar radar, List<BlipEvent> blipEvents) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.radar = radar;
    this.blipEvents = blipEvents;
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

  public Long getQuadrantId() {
    return blipEvents.get(0).getQuadrant().getId();
  }

  public Long getRingId() {
    return blipEvents.get(0).getRing().getId();
  }

  public List<BlipEvent> getBlipEvents() {
    return blipEvents;
  }

  public void setBlipEvents(List<BlipEvent> blipEvents) {
    this.blipEvents = blipEvents;
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
    return id != null && id.equals(blip.id);
  }

  @Override
  public int hashCode() {
    return 11;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", Blip.class.getSimpleName() + "[", "]")
        .add("id=" + id)
        .add("name='" + name + "'")
        .add("description='" + description + "'")
        .add("radar=" + radar)
        .toString();
  }
}
