package ru.hh.techradar.entity;

import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class Container {
  private BlipEvent blipEvent;
  private Radar radar;
  private List<Quadrant> quadrants;
  private List<Ring> rings;
  private List<Blip> blips;

  public Container() {
  }

  public Container(BlipEvent blipEvent, Radar radar, List<Quadrant> quadrants, List<Ring> rings, List<Blip> blips) {
    this.blipEvent = blipEvent;
    this.radar = radar;
    this.quadrants = quadrants;
    this.rings = rings;
    this.blips = blips;
  }

  public BlipEvent getBlipEvent() {
    return blipEvent;
  }

  public void setBlipEvent(BlipEvent blipEvent) {
    this.blipEvent = blipEvent;
  }

  public Radar getRadar() {
    return radar;
  }

  public void setRadar(Radar radar) {
    this.radar = radar;
  }

  public List<Quadrant> getQuadrants() {
    return quadrants;
  }

  public void setQuadrants(List<Quadrant> quadrants) {
    this.quadrants = quadrants;
  }

  public List<Ring> getRings() {
    return rings;
  }

  public void setRings(List<Ring> rings) {
    this.rings = rings;
  }

  public List<Blip> getBlips() {
    return blips;
  }

  public void setBlips(List<Blip> blips) {
    this.blips = blips;
  }
}
