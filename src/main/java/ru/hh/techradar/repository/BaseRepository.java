package ru.hh.techradar.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import ru.hh.techradar.entity.AuditableEntity;

public interface BaseRepository<K extends Serializable, E extends AuditableEntity<K>> {

  Optional<E> findById(K id);

  void deleteById(K id);

  E update(E entity);

  E save(E entity);

  void delete(E entity);

  List<E> findAll();
}
