package ru.hh.techradar.dto;

import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ContainerDto {
  private Long blipEventId;
  private Long radarId;
  private List<QuadrantDto> quadrants;
  private List<RingDto> rings;
  private List<BlipDto> blips;

  public ContainerDto() {
  }

  public ContainerDto(Long blipEventId, Long radarId, List<QuadrantDto> quadrants, List<RingDto> rings, List<BlipDto> blips) {
    this.blipEventId = blipEventId;
    this.radarId = radarId;
    this.quadrants = quadrants;
    this.rings = rings;
    this.blips = blips;
  }

  public Long getBlipEventId() {
    return blipEventId;
  }

  public void setBlipEventId(Long blipEventId) {
    this.blipEventId = blipEventId;
  }

  public Long getRadarId() {
    return radarId;
  }

  public void setRadarId(Long radarId) {
    this.radarId = radarId;
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
