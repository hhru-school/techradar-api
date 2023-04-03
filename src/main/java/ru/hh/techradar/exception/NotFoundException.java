package ru.hh.techradar.exception;

public class NotFoundException extends RuntimeException {

  public NotFoundException(Class<?> clazz, Object object) {
    super(String.format("%s not found by id = %s", clazz.getSimpleName(), object));
  }

}
