package ru.hh.techradar.security.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public class RegisterDto {

  @Email(message = "Not valid e-mail: '${validatedValue}'")
  @Length(min = 5, max = 45, message = "E-mail contains from 5 to 45 symbols")
  private String username;
  @Pattern(
      message = "Not valid password: '${validatedValue}'. Password can contains: numbers(0-9), latin letters(a-z,A-Z) and symbols(!@#$%^&*)",
      regexp = "[0-9a-zA-Z!@#$%^&*]{3,}")
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
