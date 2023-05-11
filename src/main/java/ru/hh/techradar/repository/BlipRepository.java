package ru.hh.techradar.repository;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.hh.techradar.entity.Blip;
import ru.hh.techradar.filter.BlipFilter;
import ru.hh.techradar.filter.ComponentFilter;

@Repository
public class BlipRepository extends BaseRepositoryImpl<Long, Blip> {
  private final SessionFactory sessionFactory;

  public BlipRepository(SessionFactory sessionFactory) {
    super(sessionFactory, Blip.class);
    this.sessionFactory = sessionFactory;
  }

  public Blip findByIdAndFilter(BlipFilter filter) {
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

  public List<Blip> findActualBlipsByBlipEventId(Long blipEventId) {
    Session session = sessionFactory.openSession();
    return session.createNativeQuery("WITH RECURSIVE r AS (\n" +
                "    SELECT blip_event_id, parent_id, blip_id, quadrant_id, ring_id, 1 AS level\n" +
                "    FROM blip_event\n" +
                "    WHERE blip_event_id = :blipEventId\n" +
                "    UNION\n" +
                "    SELECT e.blip_event_id, e.parent_id,\n" +
                "           e.blip_id, e.quadrant_id, e.ring_id, r.level + 1 AS level\n" +
                "    FROM blip_event e\n" +
                "             JOIN r ON (r.parent_id = e.blip_event_id)\n" +
                ")\n" +
                "SELECT DISTINCT ON (r.blip_id) b.blip_id, b.name, b.description, b.radar_id, b.creation_time, b.last_change_time, r.quadrant_id, " +
                "r.ring_id FROM r LEFT JOIN blip b ON r.blip_id = b.blip_id\n" +
                "ORDER BY r.blip_id, r.level;"
            , Blip.class)
        .setParameter("blipEventId", blipEventId)
        .getResultList();
  }
}
