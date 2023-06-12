package ru.hh.techradar.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "blip_template")
public class BlipTemplate extends AuditableEntity<String> {
  @Id
  @Column(name = "name")
  private String name;

  @Column(name = "description", nullable = false)
  private String description;

  public BlipTemplate(String name, String description) {
    this.name = name;
    this.description = description;
  }

  public BlipTemplate(Instant creationTime, Instant lastChangeTime, String name, String description) {
    super(creationTime, lastChangeTime);
    this.name = name;
    this.description = description;
  }

  public BlipTemplate() {
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

}
