package ru.hh.techradar.mapper;

import java.util.Collection;
import java.util.List;
public abstract class AbstractMapper<T, S> implements BaseMapper<T, S> {
  @Override
  public List<S> toDtos(Collection<T> entities) {
    return entities.stream().map(this::toDto).toList();
  }

  @Override
  public List<T> toEntities(Collection<S> dtos) {
    return dtos.stream().map(this::toEntity).toList();
  }
}
