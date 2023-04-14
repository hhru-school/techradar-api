package ru.hh.techradar.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.hibernate.SessionFactory;
import ru.hh.techradar.entity.AuditableEntity;

public abstract class BaseRepositoryImpl<K extends Serializable, E extends AuditableEntity<K>>
    implements BaseRepository<K, E> {

  private final SessionFactory sessionFactory;
  private final Class<E> entityClass;

  public BaseRepositoryImpl(SessionFactory sessionFactory, Class<E> entityClass) {
    this.sessionFactory = sessionFactory;
    this.entityClass = entityClass;
  }

  @Override
  public Optional<E> findById(K id) {
    return Optional.ofNullable(sessionFactory.getCurrentSession().get(entityClass, id));
  }

  @Override
  public void deleteById(K id) {
    E entity = sessionFactory.getCurrentSession().get(entityClass, id);
    if (Objects.nonNull(entity)) {
      sessionFactory.getCurrentSession().remove(entity);
    }
  }

  @Override
  public E update(E entity) {
    return sessionFactory.getCurrentSession().merge(entity);
  }

  @Override
  public E save(E entity) {
    sessionFactory.getCurrentSession().persist(entity);
    return entity;
  }

  @Override
  public void delete(E entity) {
    sessionFactory.getCurrentSession().remove(entity);
  }

  @Override
  public List<E> findAll() {
    return sessionFactory.getCurrentSession()
        .createQuery("SELECT entity FROM " + entityClass.getSimpleName() + " entity ORDER BY entity.id", entityClass)
        .getResultList();
  }
}
