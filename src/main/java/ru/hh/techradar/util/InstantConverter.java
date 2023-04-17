package ru.hh.techradar.util;

import jakarta.ws.rs.ext.ParamConverter;
import java.time.Instant;
import java.util.Objects;

public class InstantConverter implements ParamConverter<Instant> {

  @Override
  public Instant fromString(String value) {
    if (Objects.isNull(value)) {
      return null;
    } else {
      return Instant.parse(value);
    }
  }

  @Override
  public String toString(Instant value) {
    return Objects.nonNull(value) ?  value.toString() : null;
  }
}
