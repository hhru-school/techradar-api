package ru.hh.techradar.repository;

import java.util.List;
import java.util.Optional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
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
    Session session = sessionFactory.getCurrentSession();
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
    Session session = sessionFactory.getCurrentSession();
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
  }

  public void updateChildrenToBeBrothers(BlipEvent blipEvent) {
    Session session = sessionFactory.getCurrentSession();
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
    try (Session session = sessionFactory.openSession()) {
      return session.createNativeQuery("""
                  WITH RECURSIVE r AS (
                      SELECT be.blip_event_id, be.comment, be.parent_id, be.blip_id, be.quadrant_id, be.ring_id, be.author_id, be.radar_id,
                             be.creation_time, be.last_change_time, 1 AS level,
                             rv.radar_version_id AS radar_version_id,
                             rv.name AS radar_version_name,
                             rv.parent_id AS radar_version_parent_id,
                             ri.position AS actual_ring_position
                      FROM blip_event be
                               LEFT JOIN radar_version rv ON rv.radar_version_id = (SELECT radar_version_id
                                                                                   FROM radar_version
                                                                                   WHERE blip_event_id = :blipEventId
                                                                                   ORDER BY radar_version_id
                                                                                   LIMIT 1)
                               LEFT JOIN ring ri ON be.ring_id = ri.ring_id
                               LEFT JOIN quadrant q ON be.quadrant_id = q.quadrant_id
                      WHERE be.blip_event_id = :blipEventId
                      UNION
                      SELECT e.blip_event_id, e.comment, e.parent_id, e.blip_id, e.quadrant_id, e.ring_id, e.author_id, e.radar_id,
                             e.creation_time, e.last_change_time, r.level + 1 AS level,
                             CASE WHEN rv.radar_version_id IS NULL THEN r.radar_version_id ELSE rv.radar_version_id END AS radar_version_id,
                             CASE WHEN rv.name IS NULL THEN r.radar_version_name ELSE rv.name END AS radar_version_name,
                             CASE WHEN rv.parent_id IS NULL THEN r.radar_version_parent_id ELSE rv.parent_id END AS radar_version_parent_id,
                             ri.position AS actual_ring_position
                      FROM blip_event e
                               JOIN r ON r.parent_id = e.blip_event_id
                               LEFT JOIN radar_version rv ON e.blip_event_id = rv.blip_event_id
                               LEFT JOIN ring ri ON e.ring_id = ri.ring_id
                               LEFT JOIN quadrant q ON e.quadrant_id = q.quadrant_id
                  ), cte AS (
                      SELECT *,
                             LEAD(r.ring_id) OVER (PARTITION BY r.blip_id ORDER BY r.level) AS prev_ring_id,
                             LEAD(r.quadrant_id) OVER (PARTITION BY r.blip_id ORDER BY r.level) AS prev_quadrant_id
                      FROM r
                  )
                  SELECT DISTINCT ON (cte.blip_event_id)
                      cte.blip_event_id, cte.comment, cte.parent_id, cte.blip_id, cte.quadrant_id, cte.ring_id,
                      cte.author_id, cte.radar_id, cte.creation_time, cte.last_change_time, cte.radar_version_name AS radar_version,
                      CASE
                          WHEN cte.blip_id IS NULL THEN 'INITIAL'
                          WHEN cte.ring_id IS NULL THEN 'DELETE'
                          WHEN cte.quadrant_id != cte.prev_quadrant_id THEN 'SEC_MOVE'
                          WHEN cte.ring_id = cte.prev_ring_id THEN 'FIXED'
                          WHEN cte.prev_ring_id IS NULL THEN 'NEW'
                          WHEN ri.position > cte.actual_ring_position THEN 'FORWARD'
                          ELSE 'BACKWARD'
                      END AS draw_info
                  FROM cte
                           LEFT JOIN ring ri ON cte.prev_ring_id = ri.ring_id
                  ORDER BY cte.blip_event_id, cte.level DESC;
                  """
              , BlipEvent.class)
          .setParameter("blipEventId", blipEventId)
          .getResultList();
    }
  }

  public List<BlipEvent> findBlipEventsOfTheBlip(Long blipId, Long blipEventId) {
    try (Session session = sessionFactory.openSession()) {
      return session.createNativeQuery("""
                  WITH RECURSIVE r AS (
                      SELECT be.blip_event_id, be.comment, be.parent_id, be.blip_id, be.quadrant_id, be.ring_id, be.author_id, be.radar_id,
                             be.creation_time, be.last_change_time, 1 AS level,
                             rv.radar_version_id AS radar_version_id,
                             rv.name AS radar_version_name,
                             rv.parent_id AS radar_version_parent_id,
                             ri.position AS actual_ring_position
                      FROM blip_event be
                               LEFT JOIN radar_version rv ON rv.radar_version_id = (SELECT radar_version_id
                                                                                    FROM radar_version
                                                                                    WHERE blip_event_id = :blipEventId
                                                                                    ORDER BY radar_version_id
                                                                                    LIMIT 1)
                               LEFT JOIN ring ri ON be.ring_id = ri.ring_id
                               LEFT JOIN quadrant q ON be.quadrant_id = q.quadrant_id
                      WHERE be.blip_event_id = :blipEventId
                      UNION
                      SELECT e.blip_event_id, e.comment, e.parent_id, e.blip_id, e.quadrant_id, e.ring_id, e.author_id, e.radar_id,
                             e.creation_time, e.last_change_time, r.level + 1 AS level,
                             CASE WHEN rv.radar_version_id IS NULL THEN r.radar_version_id ELSE rv.radar_version_id END AS radar_version_id,
                             CASE WHEN rv.name IS NULL THEN r.radar_version_name ELSE rv.name END AS radar_version_name,
                             CASE WHEN rv.parent_id IS NULL THEN r.radar_version_parent_id ELSE rv.parent_id END AS radar_version_parent_id,
                             ri.position AS actual_ring_position
                      FROM blip_event e
                               JOIN r ON r.parent_id = e.blip_event_id
                               LEFT JOIN radar_version rv ON e.blip_event_id = rv.blip_event_id
                               LEFT JOIN ring ri ON e.ring_id = ri.ring_id
                               LEFT JOIN quadrant q ON e.quadrant_id = q.quadrant_id
                  ), cte AS (
                      SELECT *,
                             LEAD(r.ring_id) OVER (PARTITION BY r.blip_id ORDER BY r.level) AS prev_ring_id,
                             LEAD(r.quadrant_id) OVER (PARTITION BY r.blip_id ORDER BY r.level) AS prev_quadrant_id
                      FROM r
                  )
                  SELECT DISTINCT ON (cte.blip_event_id)
                      cte.blip_event_id, cte.comment, cte.parent_id, cte.blip_id, cte.quadrant_id, cte.ring_id,
                      cte.author_id, cte.radar_id, cte.creation_time, cte.last_change_time, cte.radar_version_name AS radar_version,
                      CASE
                          WHEN cte.blip_id IS NULL THEN 'INITIAL'
                          WHEN cte.ring_id IS NULL THEN 'DELETE'
                          WHEN cte.quadrant_id != cte.prev_quadrant_id THEN 'SEC_MOVE'
                          WHEN cte.ring_id = cte.prev_ring_id THEN 'FIXED'
                          WHEN cte.prev_ring_id IS NULL THEN 'NEW'
                          WHEN ri.position > cte.actual_ring_position THEN 'FORWARD'
                          ELSE 'BACKWARD'
                          END AS draw_info
                  FROM cte
                           LEFT JOIN ring ri ON cte.prev_ring_id = ri.ring_id
                  WHERE blip_id = :blipId
                  ORDER BY cte.blip_event_id, cte.level DESC;"""
              , BlipEvent.class)
          .setParameter("blipEventId", blipEventId)
          .setParameter("blipId", blipId)
          .getResultList();
    }
  }

  public List<BlipEvent> findChildren(Long blipEventId) {
    Session session = sessionFactory.getCurrentSession();
    return session.createQuery("""
            SELECT be FROM BlipEvent be WHERE parentId = :blipEventId
            """, BlipEvent.class)
        .setParameter("blipEventId", blipEventId)
        .getResultList();
  }
}
