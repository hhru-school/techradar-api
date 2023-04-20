package ru.hh.techradar.repository;

import java.time.Instant;
import java.util.Optional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.hh.techradar.entity.BlipEvent;

@Repository
public class BlipEventRepository extends BaseRepositoryImpl<Long, BlipEvent> {
  private final SessionFactory sessionFactory;

  public BlipEventRepository(SessionFactory sessionFactory) {
    super(sessionFactory, BlipEvent.class);
    this.sessionFactory = sessionFactory;
  }

  public Optional<BlipEvent> findActualBlipEventByBlipIdAndActualDate(Long blipId, Instant actualDate) {
    Session session = sessionFactory.openSession();
    return Optional.ofNullable(session.createQuery(
            "SELECT e FROM BlipEvent e " +
                "WHERE e.blip.id = :blipId AND e.creationTime <= :actualDate " +
                "ORDER BY e.creationTime DESC", BlipEvent.class)
        .setMaxResults(1)
        .setParameter("blipId", blipId)
        .setParameter("actualDate", actualDate)
        .getSingleResult());
  }
}
