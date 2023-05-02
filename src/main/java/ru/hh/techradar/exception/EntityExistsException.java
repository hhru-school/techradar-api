package ru.hh.techradar.exception;

public class EntityExistsException extends RuntimeException {

  public EntityExistsException(Class<?> clazz, Object object) {
    super(String.format("%s already exist with value=%s", clazz.getSimpleName(), object));
  }
}