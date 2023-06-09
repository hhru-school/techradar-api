package ru.hh.techradar.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import ru.hh.techradar.enumeration.DrawInfoType;

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
  @OneToMany(mappedBy = "blip", orphanRemoval = true)
  private List<BlipEvent> blipEvents = new ArrayList<>();
  @Column(name = "quadrant_id")
  private Long quadrantId;
  @Column(name = "ring_id")
  private Long ringId;
  @Enumerated(EnumType.STRING)
  @Column(name = "draw_info")
  private DrawInfoType drawInfo;

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
    return quadrantId;
  }

  public Long getRingId() {
    return ringId;
  }

  public List<BlipEvent> getBlipEvents() {
    return blipEvents;
  }

  public DrawInfoType getDrawInfo() {
    return drawInfo;
  }

  public void setDrawInfo(DrawInfoType drawInfo) {
    this.drawInfo = drawInfo;
  }

  public void setBlipEvents(List<BlipEvent> blipEvents) {
    this.blipEvents = blipEvents;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Blip blip)) {
      return false;
    }
    return name.equals(blip.name) && radar.equals(blip.radar);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, radar);
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
