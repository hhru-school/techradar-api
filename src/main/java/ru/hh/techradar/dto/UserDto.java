package ru.hh.techradar.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Objects;
import ru.hh.techradar.enumeration.Role;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {

  private Long id;
  private String username;
  private Role role;

  public UserDto() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserDto userDto = (UserDto) o;
    return Objects.equals(username, userDto.username);
  }

  @Override
  public int hashCode() {
    return Objects.hash(username);
  }
}
