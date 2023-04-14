package ru.hh.techradar.mapper;

import java.util.Collection;
import java.util.List;

public interface BaseMapper<T, S> {

  T toEntity(S dto);

  S toDto(T entity);

  List<S> toDtos(Collection<T> entities);

  List<T> toEntities(Collection<S> dtos);
}
