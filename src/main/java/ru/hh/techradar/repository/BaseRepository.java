package ru.hh.techradar.repository;

import java.util.List;
import java.util.Optional;

public interface BaseRepository<T> {

  Optional<T> findById(Long id);

  void deleteById(Long id);

  T update(T entity);

  T save(T entity);

  void delete(T entity);

  List<T> findAll();
}
