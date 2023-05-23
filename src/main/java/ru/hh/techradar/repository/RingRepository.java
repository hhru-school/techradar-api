package ru.hh.techradar.repository;

import jakarta.inject.Inject;
import java.util.List;
import java.util.Optional;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.hh.techradar.entity.Ring;
import ru.hh.techradar.filter.ComponentFilter;
import ru.hh.techradar.filter.DateIdFilter;

@Repository
public class RingRepository extends BaseRepositoryImpl<Long, Ring> {
  private final SessionFactory sessionFactory;

  @Inject
  public RingRepository(SessionFactory sessionFactory) {
    super(sessionFactory, Ring.class);
    this.sessionFactory = sessionFactory;
  }

  public List<Ring> findAllByFilter(ComponentFilter filter) {
    return sessionFactory.getCurrentSession()
        .createQuery("SELECT r FROM Ring r " +
                "LEFT JOIN FETCH r.settings rs " +
                "WHERE r.radar.id = :radarId " +
                "AND r.creationTime <= :actualDate " +
                "AND (r.removedAt IS NULL OR r.removedAt > :actualDate) " +
                "AND rs.creationTime IN(SELECT max(rs2.creationTime) FROM RingSetting rs2 WHERE rs2.ring.id = r.id " +
                "AND rs2.creationTime <= :actualDate) " +
                "ORDER BY rs.position"
            , Ring.class)
        .setParameter("radarId", filter.getRadarId())
        .setParameter("actualDate", filter.getActualDate())
        .getResultList();
  }

  public Optional<Ring> findById(Long id) {
    return sessionFactory.getCurrentSession()
        .createQuery("SELECT r FROM Ring r LEFT JOIN FETCH r.settings s WHERE r.id = :id", Ring.class)
        .setParameter("id", id)
        .uniqueResultOptional();
  }

  public Boolean isContainBlipsByFilter(DateIdFilter filter) {
    return sessionFactory.getCurrentSession().createQuery(
            "SELECT COUNT(be) FROM BlipEvent be " +
                "JOIN be.ring r " +
                "WHERE r.id = :ringId AND be.creationTime <= :actualDate " +
                "AND (r.removedAt IS NULL OR r.removedAt > :actualDate) "
            , Long.class)
        .setParameter("ringId", filter.getId())
        .setParameter("actualDate", filter.getDate())
        .getSingleResult() > 0;
  }

  public Boolean isAnotherSuchRingExistByFilter(Ring ring, DateIdFilter filter) {
    return sessionFactory.getCurrentSession().createQuery(
            "SELECT COUNT(rs) FROM RingSetting rs " +
                "JOIN rs.ring r " +
                "WHERE r.radar.id = :radarId AND rs.name = :supposedName " +
                "AND (r.removedAt IS NULL OR r.removedAt > :actualDate) " +
                "AND r.creationTime <= :actualDate " +
                "AND rs.creationTime IN(SELECT max(_rs.creationTime) FROM RingSetting _rs WHERE _rs.ring.id = r.id " +
                "AND _rs.creationTime <= :actualDate) "
            , Long.class)
        .setParameter("radarId", filter.getId())
        .setParameter("actualDate", filter.getDate())
        .setParameter("supposedName", ring.getCurrentSetting().getName())
        .getSingleResult() > 0;
  }
}

