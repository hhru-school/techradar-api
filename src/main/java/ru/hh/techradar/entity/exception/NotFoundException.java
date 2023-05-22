package ru.hh.techradar.entity.exception;

public class NotFoundException extends RuntimeException {

  public NotFoundException(Class<?> clazz, Object object) {
    super(String.format("%s not found by property = %s", clazz.getSimpleName(), object));
  }
}
