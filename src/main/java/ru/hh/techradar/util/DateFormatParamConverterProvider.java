package ru.hh.techradar.util;

import jakarta.ws.rs.ext.ParamConverter;
import jakarta.ws.rs.ext.ParamConverterProvider;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.time.Instant;

public class DateFormatParamConverterProvider implements ParamConverterProvider {

  @Override
  @SuppressWarnings("unchecked")
  public <T> ParamConverter<T> getConverter(Class<T> classType, Type type, Annotation[] annotations) {
    if (Instant.class.equals(classType)) {
      return (ParamConverter<T>) new InstantConverter();
    }
    return null;
  }
}
