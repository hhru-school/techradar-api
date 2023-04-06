package ru.hh.techradar.service;

public interface Service<T> {

  T findById(Long id);

  void deleteById(Long id);

  T update(Long id, T entity);

  T save(T entity);
}
