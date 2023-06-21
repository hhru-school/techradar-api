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
import jakarta.validation.constraints.NotNull;
import java.util.StringJoiner;
import ru.hh.techradar.validation.ValidName;
import ru.hh.techradar.validation.ValidPosition;

@Entity
@Table(name = "ring")
public class Ring extends AuditableEntity<Long> {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ring_id", nullable = false)
  private Long id;

  @Column(name = "name", nullable = false)
  @ValidName
  private String name;

  @Column(name = "position", nullable = false)
  @ValidPosition
  private Integer position;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "radar_id", nullable = false)
  @NotNull(message = "Radar should be not null")
  private Radar radar;

  public Ring(String name, Integer position, Radar radar) {
    this.name = name;
    this.radar = radar;
    this.position = position;
  }

  public Ring() {
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

  public Integer getPosition() {
    return position;
  }

  public void setPosition(Integer position) {
    this.position = position;
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
    return id != null && id.equals(ring.id);
  }

  @Override
  public int hashCode() {
    return 11;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", Ring.class.getSimpleName() + "[", "]")
        .add("id=" + id)
        .add("name='" + name + "'")
        .add("position=" + position)
        .toString();
  }
}
