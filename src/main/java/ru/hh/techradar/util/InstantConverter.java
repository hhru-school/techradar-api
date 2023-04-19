package ru.hh.techradar.util;

import jakarta.ws.rs.ext.ParamConverter;
import java.time.Instant;
import java.util.Optional;

public class InstantConverter implements ParamConverter<Instant> {

  @Override
  public Instant fromString(String value) {
    return Optional.ofNullable(value).map(Instant::parse).orElse(null);
  }

  @Override
  public String toString(Instant value) {
    return Optional.ofNullable(value).map(Instant::toString).orElse(null);
  }
}
