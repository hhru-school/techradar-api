package ru.hh.techradar.repository;

import java.time.Instant;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.hh.techradar.entity.Blip;

@Repository
public class BlipRepository extends BaseRepositoryImpl<Long, Blip> {
  private final SessionFactory sessionFactory;

  public BlipRepository(SessionFactory sessionFactory) {
    super(sessionFactory, Blip.class);
    this.sessionFactory = sessionFactory;
  }
  public Blip findByIdAndActualDate(Long blipId, Instant actualDate) {
    Session session = sessionFactory.openSession();
    return session.createQuery("SELECT b FROM Blip b " +
            "LEFT JOIN FETCH b.blipEvents s " +
            "WHERE b.id = :blipId and s.creationTime <= :actualDate " +
            "order by s.creationTime desc", Blip.class)
        .setParameter("blipId", blipId)
        .setParameter("actualDate", actualDate)
        .getSingleResult();
  }

  public List<Blip> findActualBlipsByRadarIdAndActualDate(Long radarId, Instant actualDate) {
    Session session = sessionFactory.openSession();
    return session.createQuery("SELECT b FROM Blip b " +
            "LEFT JOIN FETCH b.blipEvents s " +
            "WHERE b.radar.id = :radarId and s.creationTime <= :actualDate " +
            "order by s.creationTime desc", Blip.class)
        .setParameter("radarId", radarId)
        .setParameter("actualDate", actualDate)
        .getResultList();
  }
}
