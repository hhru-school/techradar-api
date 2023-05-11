package ru.hh.techradar.repository;

import java.util.List;
import java.util.Optional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.hh.techradar.entity.Blip;
import ru.hh.techradar.entity.BlipEvent;
import ru.hh.techradar.filter.BlipFilter;

@Repository
public class BlipEventRepository extends BaseRepositoryImpl<Long, BlipEvent> {
  private final SessionFactory sessionFactory;

  public BlipEventRepository(SessionFactory sessionFactory) {
    super(sessionFactory, BlipEvent.class);
    this.sessionFactory = sessionFactory;
  }

  public Optional<BlipEvent> findActualBlipEventByFilter(BlipFilter filter) {
    Session session = sessionFactory.openSession();
    return Optional.ofNullable(session.createQuery(
            "SELECT e FROM BlipEvent e " +
                "WHERE e.blip.id = :blipId AND e.creationTime <= :actualDate " +
                "ORDER BY e.creationTime DESC", BlipEvent.class)
        .setMaxResults(1)
        .setParameter("blipId", filter.getBlipId())
        .setParameter("actualDate", filter.getActualDate())
        .getSingleResult());
  }

  public void updateBrothersToBeChildren(BlipEvent blipEvent) {
    Session session = sessionFactory.openSession();
    List<BlipEvent> resultList = session.createQuery("""
            SELECT be FROM BlipEvent be WHERE parentId = :parentId AND id != :insertedId
            """, BlipEvent.class)
        .setParameter("parentId", blipEvent.getParentId())
        .setParameter("insertedId", blipEvent.getId())
        .getResultList();
    resultList.forEach(be -> {
      be.setParentId(blipEvent.getId());
      update(be);
    });
//    session.createQuery(
//            "UPDATE BlipEvent SET parentId = :insertedId " +
//                "WHERE parentId = :parentId " +
//                "AND id != :insertedId ")
//        .setParameter("parentId", blipEvent.getParentId())
//        .setParameter("insertedId", blipEvent.getId())
//        .executeUpdate();

//    session.createQuery("""
//    update BlipEvent
//    set
//        parentId = :insertedId
//    where
//        parentId = :parentId and
//        id != :insertedId
//    """)
//        .setParameter("parentId", blipEvent.getParentId())
//        .setParameter("insertedId", blipEvent.getId())
//        .executeUpdate();


//    session.createNativeQuery("UPDATE blip_event SET parent_id = :insertedId " +
//                "WHERE parent_id = :parentId " +
//                "AND blip_event_id != :insertedId"
//            , BlipEvent.class)
//        .setParameter("parentId", blipEvent.getParentId())
//        .setParameter("insertedId", blipEvent.getId())
//        .executeUpdate();

  }

  public void updateChildrenToBeBrothers(BlipEvent blipEvent) {
    Session session = sessionFactory.openSession();
    List<BlipEvent> resultList = session.createQuery("""
            SELECT be FROM BlipEvent be WHERE parentId = :excludedId
            """, BlipEvent.class)
        .setParameter("excludedId", blipEvent.getId())
        .getResultList();
    resultList.forEach(be -> {
      be.setParentId(blipEvent.getParentId());
      update(be);
    });
  }

  public List<BlipEvent> findAllBlipEventsByBlipEventId(Long blipEventId) {
    Session session = sessionFactory.openSession();
    return session.createNativeQuery("WITH RECURSIVE r AS (\n" +
                "    SELECT blip_event_id, comment, parent_id, blip_id, quadrant_id, ring_id, author_id, " +
                "creation_time, last_change_time, 1 AS level\n" +
                "    FROM blip_event\n" +
                "    WHERE blip_event_id = :blipEventId\n" +
                "    UNION\n" +
                "    SELECT e.blip_event_id, e.comment, e.parent_id,\n" +
                "           e.blip_id, e.quadrant_id, e.ring_id, e.author_id, e.creation_time, " +
                "e.last_change_time, r.level + 1 AS level\n" +
                "    FROM blip_event e\n" +
                "             JOIN r ON (r.parent_id = e.blip_event_id)\n" +
                ")\n" +
                "SELECT blip_event_id, comment, parent_id, blip_id, quadrant_id, " +
                "ring_id, author_id, creation_time, last_change_time FROM r ORDER BY r.level DESC;"
            , BlipEvent.class)
        .setParameter("blipEventId", blipEventId)
        .getResultList();
  }

//  public List<BlipEvent> findAllBlipEventsForBlip(Blip blip) {
//    Session session = sessionFactory.openSession();
//    return session.createNativeQuery("WITH RECURSIVE r AS (\n" +
//                "    SELECT blip_event_id, comment, parent_id, blip_id, quadrant_id, ring_id, author_id, " +
//                "creation_time, last_change_time, 1 AS level\n" +
//                "    FROM blip_event\n" +
//                "    WHERE blip_event_id = :blipEventId\n" +
//                "    UNION\n" +
//                "    SELECT e.blip_event_id, e.comment, e.parent_id,\n" +
//                "           e.blip_id, e.quadrant_id, e.ring_id, e.author_id, e.creation_time, " +
//                "e.last_change_time, r.level + 1 AS level\n" +
//                "    FROM blip_event e\n" +
//                "             JOIN r ON (r.parent_id = e.blip_event_id)\n" +
//                ")\n" +
//                "SELECT blip_event_id, comment, parent_id, blip_id, quadrant_id, " +
//                "ring_id, author_id, creation_time, last_change_time FROM r ORDER BY r.level DESC;"
//            , BlipEvent.class)
//        .setParameter("blipEventId", blipEventId)
//        .getResultList();
//  }
}
