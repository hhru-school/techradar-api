package ru.hh.techradar.exception;

public class OperationNotAllowedException extends RuntimeException {
  public OperationNotAllowedException(String message) {
    super(message);
  }
}
