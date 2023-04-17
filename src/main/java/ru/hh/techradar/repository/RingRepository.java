package ru.hh.techradar.repository;

import java.time.Instant;
import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.hh.techradar.entity.Radar;
import ru.hh.techradar.entity.Ring;
import ru.hh.techradar.service.RadarService;

@Repository
public class RingRepository extends BaseRepositoryImpl<Long, Ring> {
  private final SessionFactory sessionFactory;
  private final RadarService radarService;

  public RingRepository(SessionFactory sessionFactory, RadarService radarService) {
    super(sessionFactory, Ring.class);
    this.sessionFactory = sessionFactory;
    this.radarService = radarService;
  }

  public List<Ring> fetchRingsByRadarId(Long radarId, Instant actualDate) {
    Radar radar = radarService.findById(radarId);
    return sessionFactory.getCurrentSession()
        .createQuery("SELECT r FROM Ring r " +
            "WHERE r.radar = :radar " +
            "AND (r.removedAt IS NULL AND r.creationTime < :actualDate) OR " +
            "(r.removedAt > :actualDate AND r.creationTime < :actualDate)", Ring.class)
        .setParameter("radar", radar)
        .setParameter("actualDate", actualDate)
        .list();
  }
}