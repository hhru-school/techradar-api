package ru.hh.techradar.filter;

import jakarta.ws.rs.QueryParam;

public class UserFilter {

  @QueryParam("company-id")
  private Long companyId;

  public UserFilter() {
  }

  public Long getCompanyId() {
    return companyId;
  }

  public void setCompanyId(Long companyId) {
    this.companyId = companyId;
  }
}
