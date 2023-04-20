package ru.hh.techradar.repository;

import java.time.Instant;
import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.hh.techradar.entity.Ring;
import ru.hh.techradar.entity.RingSetting;
import ru.hh.techradar.util.Pair;

@Repository
public class RingRepositoryUnited {
  private final SessionFactory sessionFactory;

  public RingRepositoryUnited(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  public Pair<Ring, RingSetting> update(Pair<Ring, RingSetting> entity) {
    var currentSession = sessionFactory.getCurrentSession();
    return new Pair<>(
        currentSession.merge(entity.getFirst()),
        currentSession.merge(entity.getSecond())
    );
  }

  public List<Pair<Ring, RingSetting>> fetchPairsByRadarIdAndDate(Long radarId, Instant actualDate) {
    var currentSession = sessionFactory.getCurrentSession();
    List<Ring> rings = currentSession.createQuery("SELECT r FROM Ring r WHERE r.radar.id = :radarId " +
            "AND r.creationTime < :actualDate AND (r.removedAt IS NULL OR r.removedAt > :actualDate)", Ring.class)
        .setParameter("radarId", radarId)
        .setParameter("actualDate", actualDate)
        .list();

    return rings.stream().map(currentRing -> {
      var actualRingSetting = currentSession.createQuery("SELECT rs FROM RingSetting rs WHERE rs.ring.id = :ringId " +
              "AND rs.creationTime < :actualDate ORDER BY rs.creationTime ASC LIMIT 1", RingSetting.class)
          .setParameter("actualDate", actualDate)
          .setParameter("ringId", currentRing.getId())
          .uniqueResultOptional().orElseThrow();
      return new Pair<>(currentRing, actualRingSetting);
    }).toList();
  }
}
