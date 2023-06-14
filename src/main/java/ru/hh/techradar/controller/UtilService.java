package ru.hh.techradar.controller;

import org.springframework.security.core.context.SecurityContextHolder;

public class UtilService {
  private UtilService() {
  }
  public static String getUsername() {
    return SecurityContextHolder.getContext().getAuthentication().getName();
  }
}
