package ru.hh.techradar.dto;

public class RadarCreateDto {
  private String name;
  private Long authorId;
  private Long companyId;

  public RadarCreateDto() {
  }

  public RadarCreateDto(String name, Long authorId, Long companyId) {
    this.name = name;
    this.authorId = authorId;
    this.companyId = companyId;
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
