package ru.hh.techradar.dto;

import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Component;

@Component
public class ContainerDto {
  private Long blipEventId;
  private RadarDto radar;
  private List<QuadrantDto> quadrants;
  private List<RingDto> rings;
  private List<BlipDto> blips;

  public ContainerDto() {
  }

  public ContainerDto(Long blipEventId, RadarDto radar, List<QuadrantDto> quadrants, List<RingDto> rings, List<BlipDto> blips) {
    this.blipEventId = blipEventId;
    this.radar = radar;
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

  public List<BlipDto> getBlips() {
    return blips;
  }

  public void setBlips(List<BlipDto> blips) {
    this.blips = blips;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ContainerDto that)) {
      return false;
    }
    return radar.equals(that.radar) && Objects.equals(quadrants, that.quadrants) && Objects.equals(
        rings,
        that.rings
    ) && Objects.equals(blips, that.blips);
  }

  @Override
  public int hashCode() {
    return Objects.hash(radar, quadrants, rings, blips);
  }
}
