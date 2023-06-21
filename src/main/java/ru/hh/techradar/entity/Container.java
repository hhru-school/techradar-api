package ru.hh.techradar.entity;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

public class Container {
  private BlipEvent blipEvent;
  @NotNull(message = "Radar should be not null")
  private Radar radar;
  @NotNull(message = "Quadrant list should be not null")
  private List<Quadrant> quadrants;
  @NotNull(message = "Ring list should be not null")
  private List<Ring> rings;
  @NotNull(message = "Blip list should be not null")
  private List<Blip> blips;
  private RadarVersion radarVersion;

  public Container() {
  }

  public Container(
      BlipEvent blipEvent,
      @NotNull Radar radar,
      @NotNull List<Quadrant> quadrants,
      @NotNull List<Ring> rings,
      @NotNull List<Blip> blips,
      RadarVersion radarVersion) {
    this.blipEvent = blipEvent;
    this.radar = radar;
    this.quadrants = quadrants;
    this.rings = rings;
    this.blips = blips;
    this.radarVersion = radarVersion;
  }

  public BlipEvent getBlipEvent() {
    return blipEvent;
  }

  public void setBlipEvent(BlipEvent blipEvent) {
    this.blipEvent = blipEvent;
  }

  public @NotNull Radar getRadar() {
    return radar;
  }

  public void setRadar(@NotNull Radar radar) {
    this.radar = radar;
  }

  public @NotNull List<Quadrant> getQuadrants() {
    return quadrants;
  }

  public void setQuadrants(@NotNull List<Quadrant> quadrants) {
    this.quadrants = quadrants;
  }

  public @NotNull List<Ring> getRings() {
    return rings;
  }

  public void setRings(@NotNull List<Ring> rings) {
    this.rings = rings;
  }

  public @NotNull List<Blip> getBlips() {
    return blips;
  }

  public void setBlips(@NotNull List<Blip> blips) {
    this.blips = blips;
  }

  public RadarVersion getRadarVersion() {
    return radarVersion;
  }

  public void setRadarVersion(RadarVersion radarVersion) {
    this.radarVersion = radarVersion;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Container container)) {
      return false;
    }
    return Objects.equals(blipEvent, container.blipEvent)
        && radar.equals(container.radar)
        && Objects.equals(quadrants, container.quadrants)
        && Objects.equals(rings, container.rings)
        && Objects.equals(blips, container.blips);
  }

  @Override
  public int hashCode() {
    return Objects.hash(blipEvent, radar, quadrants, rings, blips);
  }
}
