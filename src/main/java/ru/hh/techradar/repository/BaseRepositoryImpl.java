package ru.hh.techradar.repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.hibernate.SessionFactory;

public abstract class BaseRepositoryImpl<T> implements BaseRepository<T> {

  public final SessionFactory sessionFactory;
  final Class<T> entityClass;

  public BaseRepositoryImpl(SessionFactory sessionFactory, Class<T> entityClass) {
    this.sessionFactory = sessionFactory;
    this.entityClass = entityClass;
  }

  @Override
  public Optional<T> findById(Long id) {
    return Optional.ofNullable(sessionFactory.getCurrentSession().get(entityClass, id));
  }

  @Override
  public void deleteById(Long id) {
    T entity = sessionFactory.getCurrentSession().get(entityClass, id);
    if (Objects.nonNull(entity)) {
      sessionFactory.getCurrentSession().remove(entity);
    }
  }

  @Override
  public T update(T entity) {
    return sessionFactory.getCurrentSession().merge(entity);
  }

  @Override
  public T save(T entity) {
    sessionFactory.getCurrentSession().persist(entity);
    return entity;
  }

  @Override
  public void delete(T entity) {
    sessionFactory.getCurrentSession().remove(entity);
  }

  @Override
  public List<T> findAll() {
    return sessionFactory.getCurrentSession()
        .createQuery("SELECT entity FROM " + entityClass.getSimpleName() + " entity ORDER BY entity.id", entityClass)
        .getResultList();
  }
}
