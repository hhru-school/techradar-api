package ru.hh.techradar.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;
import ru.hh.techradar.validation.ValidName;

@Entity
@Table(name = "radar")
public class Radar extends AuditableEntity<Long> {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "radar_id", nullable = false)
  private Long id;

  @Column(name = "name", nullable = false)
  @ValidName
  private String name;

  @ManyToOne
  @JoinColumn(name = "company_id", nullable = false)
  @NotNull(message = "Company should be not null")
  private Company company;

  @ManyToOne
  @JoinColumn(name = "author_id", nullable = false)
  @NotNull(message = "Author should be not null")
  private User author;

  public Radar() {
  }

  public Radar(String name, @NotNull Company company, @NotNull User author) {
    this.name = name;
    this.company = company;
    this.author = author;
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

  public @NotNull Company getCompany() {
    return company;
  }

  public void setCompany(@NotNull Company company) {
    this.company = company;
  }

  public @NotNull User getAuthor() {
    return author;
  }

  public void setAuthor(@NotNull User author) {
    this.author = author;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Radar radar)) {
      return false;
    }
    return name.equals(radar.name) && company.equals(radar.company);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, company);
  }
}
