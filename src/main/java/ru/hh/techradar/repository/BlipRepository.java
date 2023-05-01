package ru.hh.techradar.repository;

import java.time.Instant;
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
    return session.createQuery("SELECT b FROM Blip b WHERE b.id = :blipId", Blip.class)
        .setParameter("blipId", blipId)
        .getSingleResult();
  }

//  public List<Blip> findActualBlipsByRadarIdAndActualDate(Long radarId, Long blipEventTestId) {
//    Session session = sessionFactory.openSession();
//    List<Blip> blipEventTestId1 = session.createNativeQuery("WITH RECURSIVE r AS (\n" +
//            "    SELECT blip_event_test_id, parent_id, blip_id, quadrant_id, ring_id, 1 AS level\n" +
//            "    FROM blip_event_test\n" +
//            "    WHERE blip_event_test_id = :blipEventTestId\n" +
//            "    UNION\n" +
//            "    SELECT e.blip_event_test_id, e.parent_id,\n" +
//            "           e.blip_id, e.quadrant_id, e.ring_id, r.level + 1 AS level\n" +
//            "    FROM blip_event_test e\n" +
//            "             JOIN r ON (r.parent_id = e.blip_event_test_id)\n" +
//            ")\n" +
//            "SELECT DISTINCT ON (r.blip_id) b.blip_id, b.name, b.description, b.radar_id, b.creation_time, b.last_change_time, r.quadrant_id, " +
//            "r.ring_id FROM r LEFT JOIN blip" +
//            " b ON r.blip_id = b.blip_id\n" +
//            "ORDER BY r.blip_id, r.level;", Blip.class)
//        .setParameter("blipEventTestId", blipEventTestId)
//        .list();
//    return blipEventTestId1;
//  }

//  public List<Blip> findActualBlipsByRadarId(Long radarId) {
//    return findActualBlipsByRadarIdAndActualDate(radarId, Instant.now());
//  }
}
