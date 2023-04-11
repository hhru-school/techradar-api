package ru.hh.techradar.repository;

import jakarta.inject.Inject;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.hh.techradar.entity.BlipEvent;

@Repository
public class BlipEventRepository extends BaseRepositoryImpl<Long, BlipEvent> {

  private final SessionFactory sessionFactory;

  @Inject
  public BlipEventRepository(SessionFactory sessionFactory) {
    super(sessionFactory, BlipEvent.class);
    this.sessionFactory = sessionFactory;
  }
}
