package ru.hh.techradar.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.hh.techradar.entity.Radar;
import ru.hh.techradar.entity.Ring;
import ru.hh.techradar.entity.RingSetting;
import ru.hh.techradar.exception.NotFoundException;
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

  public Optional<List<Ring>> fetchRingsByRadarId(Long radarId) {
    Radar radar = radarService.findById(radarId);
      return Optional.of(
          sessionFactory.getCurrentSession()
          .createQuery("SELECT r FROM Ring r WHERE r.radar = :radar", Ring.class)
          .setParameter("radar", radar)
          .list()
      );
  }
}