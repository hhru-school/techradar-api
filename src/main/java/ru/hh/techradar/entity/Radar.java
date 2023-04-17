package ru.hh.techradar.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "radar")
public class Radar extends AuditableEntity<Long> {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "radar_id", nullable = false)
  private Long id;

  @Column(name = "name", nullable = false)
  private String name;

  @ManyToOne
  @JoinColumn(name = "company_id", nullable = false)
  private Company company;

  public Radar() {
  }

  public Radar(String name, Company company) {
    this.name = name;
    this.company = company;
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

  public Company getCompany() {
    return company;
  }

  public void setCompany(Company company) {
    this.company = company;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Radar radar = (Radar) o;
    return Objects.equals(id, radar.id)
        && Objects.equals(name, radar.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name);
  }
}