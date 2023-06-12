package ru.hh.techradar.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;

@Entity
@Table(name = "blip_template")
public class BlipTemplate extends AuditableEntity<String> {
  @Id
  @Column(name = "name", nullable = false)
  @NotBlank(message = "Name must contain at least one symbol!")
  private String name;

  @Column(name = "description", nullable = false)
  @NotNull(message = "Description must be not null!")
  private String description;

  public BlipTemplate(@NotBlank String name, @NotNull String description) {
    this.name = name;
    this.description = description;
  }

  public BlipTemplate(Instant creationTime, Instant lastChangeTime, @NotBlank String name, @NotNull String description) {
    super(creationTime, lastChangeTime);
    this.name = name;
    this.description = description;
  }

  public BlipTemplate() {
  }

  public @NotBlank String getName() {
    return name;
  }

  public void setName(@NotBlank String name) {
    this.name = name;
  }

  public @NotNull String getDescription() {
    return description;
  }

  public void setDescription(@NotNull String description) {
    this.description = description;
  }

}
