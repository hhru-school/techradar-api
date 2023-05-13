package ru.hh.techradar.security.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

public class AuthenticationResponse implements Serializable {

  @JsonProperty("type_token")
  private String typeToken = "Bearer";
  @JsonProperty("access_token")
  private String accessToken;
  @JsonProperty("refresh_token")
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
