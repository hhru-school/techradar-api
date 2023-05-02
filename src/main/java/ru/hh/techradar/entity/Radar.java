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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "radar")
public class Radar extends AuditableEntity<Long> {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "radar_id", nullable = false)
  private Long id;

  @Column(name = "name", nullable = false)
  private String name;

  @ManyToOne
  @JoinColumn(name = "company_id", nullable = false)
  private Company company;

  @ManyToOne
  @JoinColumn(name = "author_id", nullable = false)
  private User author;

  @OneToMany(mappedBy = "radar", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
  private List<Quadrant> quadrants = new ArrayList<>();
  @OneToMany(mappedBy = "radar", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
  private List<Ring> rings = new ArrayList<>();
  @OneToMany(mappedBy = "radar", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
  private List<Blip> blips = new ArrayList<>();


  public Radar() {
  }

  public Radar(String name, Company company, User author) {
    this.name = name;
    this.company = company;
    this.author = author;
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

  public Company getCompany() {
    return company;
  }

  public void setCompany(Company company) {
    this.company = company;
  }

  public User getAuthor() {
    return author;
  }

  public void setAuthor(User author) {
    this.author = author;
  }

  public List<Blip> getBlips() {
    return blips;
  }

  public void setBlips(List<Blip> blips) {
    this.blips = blips;
  }

  public List<Ring> getRings() {
    return rings;
  }

  public void setRings(List<Ring> rings) {
    this.rings = rings;
  }

  public List<Quadrant> getQuadrants() {
    return quadrants;
  }

  public void setQuadrants(List<Quadrant> quadrants) {
    this.quadrants = quadrants;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Radar radar = (Radar) o;
    return Objects.equals(id, radar.id)
        && Objects.equals(name, radar.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name);
  }

  @Override
  public String toString() {
    return "Radar{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", company=" + company +
        ", author=" + author +
        ", blips=" + blips +
        ", rings=" + rings +
        ", quadrants=" + quadrants +
        '}';
  }
}
