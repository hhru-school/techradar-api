package ru.hh.techradar.repository;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.hh.techradar.entity.Blip;
import ru.hh.techradar.filter.BlipComponentFilter;
import ru.hh.techradar.filter.ComponentFilter;

@Repository
public class BlipRepository extends BaseRepositoryImpl<Long, Blip> {
  private final SessionFactory sessionFactory;

  public BlipRepository(SessionFactory sessionFactory) {
    super(sessionFactory, Blip.class);
    this.sessionFactory = sessionFactory;
  }

  public Blip findByIdAndFilter(BlipComponentFilter filter) {
    Session session = sessionFactory.openSession();
    return session.createQuery("SELECT b FROM Blip b " +
            "LEFT JOIN FETCH b.blipEvents s " +
            "WHERE b.id = :blipId AND s.creationTime <= :actualDate " +
            "ORDER BY s.creationTime DESC", Blip.class)
        .setParameter("blipId", filter.getBlipId())
        .setParameter("actualDate", filter.getActualDate())
        .getSingleResult();
  }

  public List<Blip> findActualBlipsByFilter(ComponentFilter filter) {
    Session session = sessionFactory.openSession();
    return session.createQuery("SELECT b FROM Blip b " +
                "LEFT JOIN FETCH b.blipEvents be " +
                "WHERE b.radar.id = :radarId AND be.creationTime IN (SELECT MAX(c.creationTime) FROM BlipEvent c " +
                "WHERE c.blip.id = b.id AND c.creationTime <= :actualDate)" +
                "ORDER BY b.name"
            , Blip.class)
        .setParameter("radarId", filter.getRadarId())
        .setParameter("actualDate", filter.getActualDate())
        .getResultList();
  }
}
