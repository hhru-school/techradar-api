package ru.hh.techradar.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Objects;

@JsonInclude(JsonInclude.Include. NON_NULL)
public class UserDto {

  private Long id;
  private String username;
  private String password;

  public UserDto() {
  }

  public UserDto(Long id, String username, String password) {
    this.id = id;
    this.username = username;
    this.password = password;
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

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
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
    return Objects.equals(id, userDto.id) && Objects.equals(username, userDto.username) && Objects.equals(
        password,
        userDto.password
    );
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, username, password);
  }
}