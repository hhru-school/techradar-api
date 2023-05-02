package ru.hh.techradar.entity;

import jakarta.persistence.CascadeType;
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
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "quadrant")
public class Quadrant extends AuditableEntity<Long> {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "quadrant_id", nullable = false)
  private Long id;
  @Column(name = "name")
  private String name;
  @Column(name = "position")
  private Integer position;

  @ManyToOne
  @JoinColumn(name = "radar_id", nullable = false)
  private Radar radar;
  @OneToMany(mappedBy = "quadrant", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
  private List<Blip> blips = new ArrayList<>();


  public Quadrant() {
  }

  public Quadrant(Instant creationTime, Instant lastChangeTime, Long id, String name, Integer position, Radar radar) {
    super(creationTime, lastChangeTime);
    this.id = id;
    this.name = name;
    this.position = position;
    this.radar = radar;
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

  public Integer getPosition() {
    return position;
  }

  public void setPosition(Integer position) {
    this.position = position;
  }

  public Radar getRadar() {
    return radar;
  }

  public void setRadar(Radar radar) {
    this.radar = radar;
  }

  public List<Blip> getBlips() {
    return blips;
  }

  public void setBlips(List<Blip> blips) {
    this.blips = blips;
  }
}
