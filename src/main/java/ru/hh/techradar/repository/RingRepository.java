package ru.hh.techradar.repository;

import jakarta.inject.Inject;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.hh.techradar.entity.Ring;

@Repository
public class RingRepository extends BaseRepositoryImpl<Long, Ring> {
  private final SessionFactory sessionFactory;

  @Inject
  public RingRepository(SessionFactory sessionFactory) {
    super(sessionFactory, Ring.class);
    this.sessionFactory = sessionFactory;
  }

  public List<Ring> findAllByFilter(Long radarId, Instant actualDate) {
    return sessionFactory.getCurrentSession()
        .createQuery("SELECT r FROM Ring r WHERE r.radar.id = :radarId ", Ring.class)
        .setParameter("radarId", radarId)
        .getResultList();
  }

  public Optional<Ring> findById(Long id) {
    return sessionFactory.getCurrentSession()
        .createQuery("SELECT r FROM Ring r WHERE r.id = :id", Ring.class)
        .setParameter("id", id)
        .uniqueResultOptional();
  }
}

