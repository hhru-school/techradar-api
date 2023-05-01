package ru.hh.techradar.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
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

  public List<Quadrant> findAllByFilter(Long radarId, Instant actualDate) {
    return sessionFactory.getCurrentSession()
        .createQuery("SELECT q FROM Quadrant q WHERE q.radar.id = :radarId ", Quadrant.class)
        .setParameter("radarId", radarId)
        .setParameter("actualDate", actualDate)
        .getResultList();
  }

  public Optional<Quadrant> findById(Long id) {
    return sessionFactory.getCurrentSession()
        .createQuery("SELECT q FROM Quadrant q WHERE q.id = :id", Quadrant.class)
        .setParameter("id", id)
        .uniqueResultOptional();
  }
}
