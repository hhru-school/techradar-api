package ru.hh.techradar.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import java.util.StringJoiner;
import ru.hh.techradar.validation.ValidName;

@Entity
@Table(name = "company")
public class Company extends AuditableEntity<Long> {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "company_id", nullable = false)
  private Long id;

  @Column(name = "name", nullable = false)
  @ValidName
  private String name;
  @ManyToMany(mappedBy = "companies")
  private Set<User> users = new HashSet<>();

  public Company() {
  }

  public Company(Long id, String name) {
    this.id = id;
    this.name = name;
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

  public Set<User> getUsers() {
    return users;
  }

  public void setUsers(Set<User> employees) {
    this.users = employees;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Company company = (Company) o;
    return id != null && id.equals(company.id);
  }

  @Override
  public int hashCode() {
    return 11;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", Company.class.getSimpleName() + "[", "]")
        .add("id=" + id)
        .add("name='" + name + "'")
        .toString();
  }
}
