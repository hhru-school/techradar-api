package ru.hh.techradar.security.dto;

import java.io.Serializable;

public class AuthenticationResponse implements Serializable {
  private String typeToken = "Bearer";
  private String accessToken;
  private String refreshToken;

  public AuthenticationResponse() {
  }

  public AuthenticationResponse(
      String accessToken,
      String refreshToken) {
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
  }

  public String getTypeToken() {
    return typeToken;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }
}
