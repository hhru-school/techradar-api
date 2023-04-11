package ru.hh.techradar.repository;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.hh.techradar.entity.Quadrant;

@Repository
public class QuadrantRepository extends BaseRepositoryImpl<Long, Quadrant> {

  private final SessionFactory sessionFactory;

  public QuadrantRepository(SessionFactory sessionFactory) {
    super(sessionFactory, Quadrant.class);
    this.sessionFactory = sessionFactory;
  }
}
