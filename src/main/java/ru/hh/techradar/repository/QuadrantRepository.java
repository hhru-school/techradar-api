package ru.hh.techradar.repository;

import java.util.List;
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

  public List<Quadrant> findAllByFilter(Long radarId) {
    return sessionFactory.getCurrentSession()
        .createQuery("SELECT quadrant FROM Quadrant quadrant WHERE quadrant.radar.id =: radarId and quadrant.removedAt IS NULL", Quadrant.class)
        .setParameter("radarId", radarId)
        .getResultList();
  }
}
