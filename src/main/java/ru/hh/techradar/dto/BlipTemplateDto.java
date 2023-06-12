package ru.hh.techradar.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BlipTemplateDto {

  @NotBlank(message = "Name must contain at least one symbol!")
  private String name;

  @NotNull(message = "Description must be not null!")
  private String description;

  public BlipTemplateDto(@NotBlank String name, @NotNull String description) {
    this.name = name;
    this.description = description;
  }

  public BlipTemplateDto() {
    description = "";
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
