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
import java.util.Objects;

@Entity
@Table(name = "ring_setting")
public class RingSetting extends AuditableEntity<Long> {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ring_setting_id", nullable = false)
  private Long id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "position", nullable = false)
  private Integer position;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ring_id", nullable = false)
  private Ring ring;

  public RingSetting() {
  }

  public RingSetting(String name, Integer position, Ring ring) {
    this.name = name;
    this.position = position;
    this.ring = ring;
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

  public Ring getRing() {
    return ring;
  }

  public void setRing(Ring ring) {
    this.ring = ring;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RingSetting ringSetting = (RingSetting) o;
    return id.equals(ringSetting.id)
        && name.equals(ringSetting.name)
        && position.equals(ringSetting.position)
        && ring.equals(ringSetting.ring)
        && getCreationTime().equals(ring.getCreationTime())
        && getLastChangeTime().equals(ring.getLastChangeTime());
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, position, ring, getCreationTime(), getLastChangeTime());
  }

  @Override
  public String toString() {
    return "RingSetting {" +
        "id=" + id +
        ", name=" + name +
        ", position=" + position +
        ", ring=" + ring +
        ", creationTime=" + getCreationTime() +
        ", lastChangeTime=" + getLastChangeTime()
        + '\n' + '}';
  }
}

