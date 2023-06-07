package ru.hh.techradar.repository;

import java.util.List;
import java.util.Optional;
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

  public List<Radar> findAllByCompanyId(Long companyId) {
    return sessionFactory.getCurrentSession()
        .createQuery("SELECT r FROM Radar r " +
                "WHERE r.company.id = :companyId " +
                "ORDER BY r.creationTime"
            , Radar.class)
        .setParameter("companyId", companyId)
        .getResultList();
  }

  public Optional<Radar> findByNameAndCompanyId(String name, Long companyId) {
    return sessionFactory.getCurrentSession()
        .createQuery("""
            SELECT r FROM Radar r WHERE r.name = :name AND r.company.id = :companyId
            """, Radar.class)
        .setParameter("name", name)
        .setParameter("companyId", companyId)
        .uniqueResultOptional();
  }
}
