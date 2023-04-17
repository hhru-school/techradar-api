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
        .createQuery("SELECT q FROM Quadrant q " +
            "LEFT JOIN FETCH q.settings s " +
            "WHERE s.id IN(" +
                        "SELECT max(setting.id) FROM Quadrant quadrant " +
                        "LEFT JOIN QuadrantSetting setting ON setting.quadrant.id = quadrant.id " +
                        "WHERE quadrant.radar.id = :radarId " +
                        "AND (quadrant.removedAt IS NULL OR (quadrant.creationTime <=:actualDate AND quadrant.removedAt > :actualDate)) " +
                        "AND setting.creationTime <= :actualDate " +
                        "GROUP BY quadrant.id" +
                        ") " +
            "ORDER BY s.position"
            , Quadrant.class)
        .setParameter("radarId", radarId)
        .setParameter("actualDate", actualDate)
        .getResultList();
  }

  public Optional<Quadrant> findById(Long id) {
    return Optional.ofNullable(sessionFactory.getCurrentSession()
        .createQuery("SELECT q FROM Quadrant q LEFT JOIN FETCH q.settings s WHERE q.id = :id", Quadrant.class)
        .setParameter("id", id)
        .getSingleResult());
  }
}
