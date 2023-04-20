package ru.hh.techradar.dto;

import java.util.List;

public class RadarDto {
  private Long id;
  private String name;
  private List<QuadrantDto> quadrants;
  private List<RingDto> rings;
  private List<BlipDto> blips;

  public RadarDto() {
  }

  public RadarDto(Long id, String name, List<QuadrantDto> quadrants, List<RingDto> rings, List<BlipDto> blips) {
    this.id = id;
    this.name = name;
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
}
