package ru.hh.techradar.dto;

import java.util.List;

public class ContainerCreateDto {
  private RadarDto radar;
  private List<QuadrantDto> quadrants;
  private List<RingDto> rings;
  private List<BlipCreateDto> blips;

  public ContainerCreateDto() {
  }

  public ContainerCreateDto(RadarDto radar, List<QuadrantDto> quadrants, List<RingDto> rings, List<BlipCreateDto> blips) {
    this.radar = radar;
    this.quadrants = quadrants;
    this.rings = rings;
    this.blips = blips;
  }

  public RadarDto getRadar() {
    return radar;
  }

  public void setRadar(RadarDto radar) {
    this.radar = radar;
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

  public List<BlipCreateDto> getBlips() {
    return blips;
  }

  public void setBlips(List<BlipCreateDto> blips) {
    this.blips = blips;
  }
}
