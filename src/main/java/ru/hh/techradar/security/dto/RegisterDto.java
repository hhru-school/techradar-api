package ru.hh.techradar.security.dto;

import jakarta.validation.constraints.Email;
import ru.hh.techradar.validation.ValidPassword;

public class RegisterDto {

  @Email(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$",
      message = "Invalid Email address")
  private String username;
  @ValidPassword
  private String password;
  private String confirmPassword;

  public RegisterDto() {
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

  public String getConfirmPassword() {
    return confirmPassword;
  }

  public void setConfirmPassword(String confirmPassword) {
    this.confirmPassword = confirmPassword;
  }
}
