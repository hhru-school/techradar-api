package ru.hh.techradar.repository;

import java.util.List;
import java.util.Optional;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.hh.techradar.entity.Quadrant;
import ru.hh.techradar.filter.ComponentFilter;

@Repository
public class QuadrantRepository extends BaseRepositoryImpl<Long, Quadrant> {
  private final SessionFactory sessionFactory;

  public QuadrantRepository(SessionFactory sessionFactory) {
    super(sessionFactory, Quadrant.class);
    this.sessionFactory = sessionFactory;
  }

  public List<Quadrant> findAllByFilter(ComponentFilter filter) {
    return sessionFactory.getCurrentSession()
        .createQuery("SELECT q FROM Quadrant q " +
                "LEFT JOIN FETCH q.settings qs " +
                "WHERE q.radar.id = :radarId " +
                "AND q.creationTime <= :actualDate " +
                "AND (q.removedAt IS NULL OR q.removedAt > :actualDate) " +
                "AND qs.creationTime IN(SELECT max(qs2.creationTime) FROM QuadrantSetting qs2 WHERE qs2.quadrant.id = q.id AND qs2.creationTime <= " +
                ":actualDate) " +
                "ORDER BY qs.position"
            , Quadrant.class)
        .setParameter("radarId", filter.getRadarId())
        .setParameter("actualDate", filter.getActualDate())
        .getResultList();
  }

  public Optional<Quadrant> findById(Long id) {
    return sessionFactory.getCurrentSession()
        .createQuery("SELECT q FROM Quadrant q LEFT JOIN FETCH q.settings s WHERE q.id = :id", Quadrant.class)
        .setParameter("id", id)
        .uniqueResultOptional();
  }
}
