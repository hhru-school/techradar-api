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
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.util.StringJoiner;

@Entity
@Table(name = "quadrant_setting")
public class QuadrantSetting extends AuditableEntity<Long> {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "quadrant_setting_id", nullable = false)
  private Long id;

  @NotNull
  @Column(name = "name", nullable = false)
  private String name;

  @NotNull
  @DecimalMax(message = "Position must be less than 8", value = "8")
  @DecimalMin(message = "Position must be bigger than 0", value = "1")
  @Column(name = "position", nullable = false)
  private Integer position;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "quadrant_id", nullable = false)
  private Quadrant quadrant;

  public QuadrantSetting() {
  }

  public QuadrantSetting(String name, Integer position, Quadrant quadrant) {
    this.name = name;
    this.position = position;
    this.quadrant = quadrant;
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

  public Quadrant getQuadrant() {
    return quadrant;
  }

  public void setQuadrant(Quadrant quadrant) {
    this.quadrant = quadrant;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    QuadrantSetting setting = (QuadrantSetting) o;
    return id != null && id.equals(setting.id);
  }

  @Override
  public int hashCode() {
    return 11;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", QuadrantSetting.class.getSimpleName() + "[", "]")
        .add("id=" + id)
        .add("name='" + name + "'")
        .add("position=" + position)
        .toString();
  }
}
