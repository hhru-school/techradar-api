package ru.hh.techradar.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import ru.hh.techradar.validation.ValidName;

@Entity
@Table(name = "radar_version")
public class RadarVersion extends AuditableEntity<Long> {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "radar_version_id", nullable = false)
  private Long id;

  @Column(name = "name", nullable = false)
  @ValidName
  private String name;

  @Column(name = "release", nullable = false)
  @NotNull(message = "Release should be not null")
  private Boolean release;
  @ManyToOne
  @JoinColumn(name = "radar_id", nullable = false)
  @NotNull(message = "Radar should be not null")
  private Radar radar;
  @ManyToOne
  @JoinColumn(name = "blip_event_id", nullable = false)
  @NotNull(message = "BlipEvent should be not null")
  private BlipEvent blipEvent;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_id")
  private RadarVersion parent;
  @Column(name = "level")
  @PositiveOrZero(message = "Level should be positive or zero")
  private Integer level;
  @Column(name = "toggle_available")
  private Boolean toggleAvailable;

  @OneToMany(mappedBy = "parent")
  private List<RadarVersion> children = new ArrayList<>();

  public RadarVersion() {
  }

  public RadarVersion(
      Instant creationTime,
      Instant lastChangeTime,
      Long id,
      String name,
      @NotNull Boolean release,
      @NotNull Radar radar,
      @NotNull BlipEvent blipEvent, RadarVersion parent, Integer level, Boolean toggleAvailable) {
    super(creationTime, lastChangeTime);
    this.id = id;
    this.name = name;
    this.release = release;
    this.radar = radar;
    this.blipEvent = blipEvent;
    this.parent = parent;
    this.level = level;
    this.toggleAvailable = toggleAvailable;
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

  public @NotNull Boolean getRelease() {
    return release;
  }

  public void setRelease(@NotNull Boolean release) {
    this.release = release;
  }

  public @NotNull Radar getRadar() {
    return radar;
  }

  public void setRadar(@NotNull Radar radar) {
    this.radar = radar;
  }

  public @NotNull BlipEvent getBlipEvent() {
    return blipEvent;
  }

  public void setBlipEvent(@NotNull BlipEvent blipEvent) {
    this.blipEvent = blipEvent;
  }

  public RadarVersion getParent() {
    return parent;
  }

  public void setParent(RadarVersion parent) {
    this.parent = parent;
  }

  public Integer getLevel() {
    return level;
  }

  public void setLevel(Integer level) {
    this.level = level;
  }

  public Boolean getToggleAvailable() {
    return toggleAvailable;
  }

  public void setToggleAvailable(Boolean toggleAvailable) {
    this.toggleAvailable = toggleAvailable;
  }

  public List<RadarVersion> getChildren() {
    return children;
  }

  public void setChildren(List<RadarVersion> radarVersions) {
    this.children = radarVersions;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof RadarVersion that)) {
      return false;
    }
    return name.equals(that.name) && radar.equals(that.radar);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, radar);
  }
}
