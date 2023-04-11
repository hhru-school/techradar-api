package ru.hh.techradar.repository;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.hh.techradar.entity.Radar;

@Repository
public class RadarRepository extends BaseRepositoryImpl<Long, Radar> {

  private final SessionFactory sessionFactory;

  public RadarRepository(SessionFactory sessionFactory) {
    super(sessionFactory, Radar.class);
    this.sessionFactory = sessionFactory;
  }
}
