package ru.hh.techradar.dto;

import java.util.List;

public class RadarDto {
  private Long id;
  private String name;
  private Long authorId;
  private Long companyId;
  private List<QuadrantDto> quadrants;
  private List<RingDto> rings;
  private List<BlipDto> blips;

  public RadarDto() {
  }

  public RadarDto(Long id, String name, Long authorId, Long companyId, List<QuadrantDto> quadrants, List<RingDto> rings, List<BlipDto> blips) {
    this.id = id;
    this.name = name;
    this.authorId = authorId;
    this.companyId = companyId;
    this.quadrants = quadrants;
    this.rings = rings;
    this.blips = blips;
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

  public List<QuadrantDto> getQuadrants() {
    return quadrants;
  }

  public void setQuadrants(List<QuadrantDto> quadrants) {
    this.quadrants = quadrants;
  }

  public List<RingDto> getRings() {
    return rings;
  }

  public void setRings(List<RingDto> rings) {
    this.rings = rings;
  }

  public List<BlipDto> getBlips() {
    return blips;
  }

  public void setBlips(List<BlipDto> blips) {
    this.blips = blips;
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
