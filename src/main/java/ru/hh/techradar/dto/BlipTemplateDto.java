package ru.hh.techradar.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BlipTemplateDto {
  private String name;
  private String description;

  public BlipTemplateDto(String name, String description) {
    this.name = name;
    this.description = description;
  }

  public BlipTemplateDto() {
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
