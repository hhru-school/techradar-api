package ru.hh.techradar.service;

import java.io.Serializable;
import java.util.List;
import ru.hh.techradar.entity.AuditableEntity;

public interface BaseService<K extends Serializable, E extends AuditableEntity<K>> {

  E findById(K id);

  void deleteById(K id);

  E update(K id, E entity);

  E save(E entity);

  List<E> findAll();
}
