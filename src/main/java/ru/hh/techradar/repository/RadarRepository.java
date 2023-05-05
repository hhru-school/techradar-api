package ru.hh.techradar.repository;

import java.time.Instant;
import java.util.List;
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

  public List<Radar> findAllByFilter(Long companyId, Instant actualDate) {
    return sessionFactory.getCurrentSession()
        .createQuery("SELECT r FROM Radar r " +
                "WHERE r.company.id = :companyId " +
                "AND r.creationTime <= :actualDate " +
                "ORDER BY r.creationTime"
            , Radar.class)
        .setParameter("companyId", companyId)
        .setParameter("actualDate", actualDate)
        .getResultList();
  }
}
