package ru.hh.techradar.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RadarDto {
  private Long id;
  private String name;
  private Long authorId;
  private Long companyId;

  public RadarDto() {
  }

  public RadarDto(
      Long id,
      String name,
      Long companyId//,
  ) {
    this.id = id;
    this.name = name;
    this.companyId = companyId;
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

  public Long getAuthorId() {
    return authorId;
  }

  public void setAuthorId(Long authorId) {
    this.authorId = authorId;
  }

  public Long getCompanyId() {
    return companyId;
  }

  public void setCompanyId(Long companyId) {
    this.companyId = companyId;
  }
}
