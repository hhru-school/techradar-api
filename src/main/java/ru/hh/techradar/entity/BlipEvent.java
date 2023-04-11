package ru.hh.techradar.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "blip_event")
public class BlipEvent extends AuditableEntity<Long> {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "blip_event_id", nullable = false)
  private Long id;

  @Column(name = "comment", nullable = false)
  private String comment;

  @Column(name = "version_name", length = 128)
  private String versionName;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "blip_id", nullable = false)
  private Blip blip;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "quadrant_id", nullable = false)
  private Quadrant quadrant;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ring_id", nullable = false)
  private Ring ring;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "author_id", nullable = false)
  private User author;

  public BlipEvent() {
  }

  public BlipEvent(String comment, String versionName, Blip blip, Quadrant quadrant, Ring ring, User author) {
    this.comment = comment;
    this.versionName = versionName;
    this.blip = blip;
    this.quadrant = quadrant;
    this.ring = ring;
    this.author = author;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public String getVersionName() {
    return versionName;
  }

  public void setVersionName(String versionName) {
    this.versionName = versionName;
  }

  public Blip getBlip() {
    return blip;
  }

  public void setBlip(Blip blip) {
    this.blip = blip;
  }

  public Quadrant getQuadrant() {
    return quadrant;
  }

  public void setQuadrant(Quadrant quadrant) {
    this.quadrant = quadrant;
  }

  public Ring getRing() {
    return ring;
  }

  public void setRing(Ring ring) {
    this.ring = ring;
  }

  public User getAuthor() {
    return author;
  }

  public void setAuthor(User author) {
    this.author = author;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BlipEvent blipEvent = (BlipEvent) o;
    return Objects.equals(id, blipEvent.id)
        && Objects.equals(comment, blipEvent.comment)
        && Objects.equals(versionName, blipEvent.versionName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, comment, versionName);
  }
}
