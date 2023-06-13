package ru.hh.techradar.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.Instant;

@Entity
@Table(name = "blip_template")
public class BlipTemplate extends AuditableEntity<String> {
  @Id
  @Column(name = "name", nullable = false)
  @Size(max = 45)
  @Pattern(regexp = "^\\S+(\\s+\\S+)*$")
  private String name;

  @Column(name = "description", nullable = false)
  @Size(max = 500)
  @NotNull(message = "Description must be not null")
  private String description;

  public BlipTemplate(String name, @NotNull String description) {
    this.name = name;
    this.description = description;
  }

  public BlipTemplate(Instant creationTime, Instant lastChangeTime, String name, @NotNull String description) {
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

  public @NotNull String getDescription() {
    return description;
  }

  public void setDescription(@NotNull String description) {
    this.description = description;
  }
}
