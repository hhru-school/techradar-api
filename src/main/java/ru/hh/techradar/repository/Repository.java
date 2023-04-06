package ru.hh.techradar.repository;

import java.util.Optional;

public interface Repository<T> {

  Optional<T> findById(Long id);

  void deleteById(Long id);

  T update(T entity);

  T save(T entity);

  void delete(T entity);
}
