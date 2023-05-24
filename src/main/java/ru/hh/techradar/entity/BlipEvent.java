package ru.hh.techradar.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.Objects;
import java.util.StringJoiner;

@Entity
@Table(name = "blip_event")
public class BlipEvent extends AuditableEntity<Long> {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "blip_event_id", nullable = false)
  private Long id;
  @Column(name = "comment")
  private String comment;
  @Column(name = "parent_id")
  private Long parentId;
  @ManyToOne
  @JoinColumn(name = "blip_id", nullable = false)
  private Blip blip;
  @ManyToOne
  @JoinColumn(name = "quadrant_id", nullable = false)
  private Quadrant quadrant;
  @ManyToOne
  @JoinColumn(name = "ring_id", nullable = false)
  private Ring ring;
  @ManyToOne
  @JoinColumn(name = "author_id", nullable = false)
  private User user;

  public BlipEvent() {
  }

  public BlipEvent(
      Long id,
      String comment,
      Blip blip,
      Quadrant quadrant,
      Ring ring,
      User user,
      Instant creationTime,
      Instant lastChangeTime, Long parentId) {
    super(creationTime, lastChangeTime);
    this.id = id;
    this.comment = comment;
    this.blip = blip;
    this.quadrant = quadrant;
    this.ring = ring;
    this.user = user;
    this.parentId = parentId;
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

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Long getParentId() {
    return parentId;
  }

  public void setParentId(Long parentId) {
    this.parentId = parentId;
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
    return id != null && Objects.equals(id, blipEvent.id);
  }

  @Override
  public int hashCode() {
    return 11;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", BlipEvent.class.getSimpleName() + "[", "]")
        .add("id=" + id)
        .add("comment='" + comment + "'")
        .add("parentId='" + parentId + "'")
        .add("blip=" + blip)
        .add("quadrant=" + quadrant)
        .add("ring=" + ring)
        .add("user=" + user)
        .toString();
  }
}
