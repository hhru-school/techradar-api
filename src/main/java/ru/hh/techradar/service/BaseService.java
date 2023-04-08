package ru.hh.techradar.service;

import java.util.List;

public interface BaseService<T> {

  T findById(Long id);

  void deleteById(Long id);

  T update(Long id, T entity);

  T save(T entity);

  List<T> findAll();
}
