package ru.hh.techradar.security.dto;

public class AuthenticationDto {
  private String username;
  private String Password;

  public AuthenticationDto() {
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return Password;
  }

  public void setPassword(String password) {
    Password = password;
  }
}
