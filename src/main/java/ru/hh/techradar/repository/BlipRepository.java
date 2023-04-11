package ru.hh.techradar.repository;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.hh.techradar.entity.Blip;

@Repository
public class BlipRepository extends BaseRepositoryImpl<Long, Blip> {

  private final SessionFactory sessionFactory;

  public BlipRepository(SessionFactory sessionFactory) {
    super(sessionFactory, Blip.class);
    this.sessionFactory = sessionFactory;
  }
}
