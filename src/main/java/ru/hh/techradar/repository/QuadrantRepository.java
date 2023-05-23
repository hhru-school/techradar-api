package ru.hh.techradar.repository;

import java.util.List;
import java.util.Optional;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.hh.techradar.entity.Quadrant;
import ru.hh.techradar.filter.ComponentFilter;
import ru.hh.techradar.filter.DateIdFilter;

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

  public Boolean isContainBlipsByFilter(DateIdFilter filter) {
    return sessionFactory.getCurrentSession().createQuery(
            "SELECT COUNT(be) FROM BlipEvent be " +
                "JOIN be.quadrant q " +
                "WHERE q.id = :quadrantId AND be.creationTime <= :actualDate "
            , Long.class)
        .setParameter("quadrantId", filter.getId())
        .setParameter("actualDate", filter.getDate())
        .getSingleResult() > 0;
  }

  public Boolean isAnotherSuchQuadrantExistByFilter(Quadrant quadrant, DateIdFilter filter) {
    return sessionFactory.getCurrentSession().createQuery(
            "SELECT COUNT(qs) FROM QuadrantSetting qs " +
                "JOIN qs.quadrant q " +
                "WHERE q.radar.id = :radarId AND qs.name = :supposedName " +
                "AND (q.removedAt IS NULL OR q.removedAt > :actualDate) " +
                "AND q.creationTime <= :actualDate " +
                "AND qs.creationTime IN(SELECT max(_qs.creationTime) FROM QuadrantSetting _qs WHERE _qs.quadrant.id = q.id " +
                "AND _qs.creationTime <= :actualDate) "
            , Long.class)
        .setParameter("radarId", filter.getId())
        .setParameter("actualDate", filter.getDate())
        .setParameter("supposedName", quadrant.getCurrentSetting().getName())
        .getSingleResult() > 0;
  }
}
