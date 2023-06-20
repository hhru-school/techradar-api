package ru.hh.techradar.repository;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.hh.techradar.entity.Blip;
import ru.hh.techradar.entity.RadarVersion;
import ru.hh.techradar.filter.BlipFilter;
import ru.hh.techradar.filter.ComponentFilter;

@Repository
public class BlipRepository extends BaseRepositoryImpl<Long, Blip> {
  private final SessionFactory sessionFactory;

  public BlipRepository(SessionFactory sessionFactory) {
    super(sessionFactory, Blip.class);
    this.sessionFactory = sessionFactory;
  }

  public Blip findByFilter(BlipFilter filter) {
    Session session = sessionFactory.getCurrentSession();
      return session.createQuery("SELECT b FROM Blip b " +
              "LEFT JOIN FETCH b.blipEvents s " +
              "WHERE b.id = :blipId AND s.creationTime <= :actualDate " +
              "ORDER BY s.creationTime DESC", Blip.class)
          .setParameter("blipId", filter.getBlipId())
          .setParameter("actualDate", filter.getActualDate())
          .getSingleResult();
  }

  public List<Blip> findActualBlipsByFilter(ComponentFilter filter) {
    Session session = sessionFactory.getCurrentSession();
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
    Session session = sessionFactory.getCurrentSession();
      return session.createNativeQuery("""
                  WITH RECURSIVE r AS (
                      SELECT blip_event_id, parent_id, blip_id, quadrant_id, ring_id, 1 AS level
                      FROM blip_event
                      WHERE blip_event_id = :blipEventId
                      UNION
                      SELECT e.blip_event_id, e.parent_id,
                             e.blip_id, e.quadrant_id, e.ring_id, r.level + 1 AS level
                      FROM blip_event e
                               JOIN r ON (r.parent_id = e.blip_event_id)
                  )
                  SELECT DISTINCT ON (r.blip_id) b.blip_id, b.name, b.description, b.radar_id, b.creation_time,
                  b.last_change_time, r.quadrant_id, r.ring_id, draw_info FROM r LEFT JOIN blip b ON r.blip_id = b.blip_id
                  WHERE r.blip_id IS NOT NULL
                  ORDER BY r.blip_id, r.level;"""
              , Blip.class)
          .setParameter("blipEventId", blipEventId)
          .getResultList();
  }

  public List<Blip> findActualBlipsByRadarVersionWithDrawInfo(RadarVersion radarVersion) {
    try (Session session = sessionFactory.openSession()) {
      return session.createNativeQuery("""
                  WITH RECURSIVE r AS (
                      SELECT be.blip_event_id, be.parent_id, be.blip_id, be.quadrant_id, be.ring_id, 1 AS level,
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
                      SELECT e.blip_event_id, e.parent_id,
                             e.blip_id, e.quadrant_id, e.ring_id, r.level + 1 AS level,
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
                                 
                  SELECT DISTINCT ON (cte.blip_id)
                      b.blip_id, b.name, b.description, b.radar_id, b.creation_time, b.last_change_time,
                      cte.quadrant_id, cte.ring_id,
                      CASE
                          WHEN cte.radar_version_id = :radarVersionId AND cte.quadrant_id != cte.prev_quadrant_id THEN 'SEC_MOVE'
                          WHEN cte.radar_version_id < :radarVersionId OR cte.ring_id = cte.prev_ring_id THEN 'FIXED'
                          WHEN cte.radar_version_id = :radarVersionId AND cte.prev_ring_id IS NULL THEN 'NEW'
                          WHEN cte.radar_version_id = :radarVersionId AND ri.position > cte.actual_ring_position THEN 'FORWARD'
                          ELSE 'BACKWARD'
                          END AS draw_info
                  FROM cte
                           LEFT JOIN blip b ON cte.blip_id = b.blip_id
                           LEFT JOIN ring ri ON cte.prev_ring_id = ri.ring_id
                  WHERE cte.blip_id IS NOT NULL
                  ORDER BY cte.blip_id, cte.level;
                  """
              , Blip.class)
          .setParameter("blipEventId", radarVersion.getBlipEvent().getId())
          .setParameter("radarVersionId", radarVersion.getId())
          .getResultList();
    }
  }
}
