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
@Table(name = "quadrant_setting")
public class QuadrantSetting extends AuditableEntity<Long> {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "quadrant_setting_id", nullable = false)
  private Long id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "position", nullable = false)
  private Integer position;

  @ManyToOne
  @JoinColumn(name = "quadrant_id", nullable = false)
  private Quadrant quadrant;

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

  public Quadrant getQuadrant() {
    return quadrant;
  }

  public void setQuadrant(Quadrant quadrant) {
    this.quadrant = quadrant;
  }

  public QuadrantSetting() {}

  public QuadrantSetting(Long id, String name, Integer position, Quadrant quadrant, Instant creationTime, Instant lastChangeTime) {
    this.id = id;
    this.name = name;
    this.position = position;
    this.quadrant = quadrant;
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
    QuadrantSetting quadrantSetting = (QuadrantSetting) o;
    return id.equals(quadrantSetting.id)
        && name.equals(quadrantSetting.name)
        && position.equals(quadrantSetting.position)
        && quadrant.equals(quadrantSetting.quadrant)
        && getCreationTime().equals(quadrant.getCreationTime())
        && getLastChangeTime().equals(quadrant.getLastChangeTime());
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, position, quadrant, getCreationTime(), getLastChangeTime());
  }

  @Override
  public String toString() {
    return "QuadrantSetting { " +
        "id=" + id +
        ", name=" + name +
        ", position=" + position +
        ", quadrant=" + quadrant +
        ", creationTime=" + getCreationTime() +
        ", lastChangeTime=" + getLastChangeTime()
        + '\n' + '}';
  }
}
