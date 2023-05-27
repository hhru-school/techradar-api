package ru.hh.techradar.repository;

import jakarta.inject.Inject;
import java.util.List;
import java.util.Optional;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.hh.techradar.entity.Ring;
import ru.hh.techradar.filter.ComponentFilter;

@Repository
public class RingRepository extends BaseRepositoryImpl<Long, Ring> {
  private final SessionFactory sessionFactory;

  @Inject
  public RingRepository(SessionFactory sessionFactory) {
    super(sessionFactory, Ring.class);
    this.sessionFactory = sessionFactory;
  }

  public List<Ring> findAllByFilter(ComponentFilter filter) {
    return sessionFactory.getCurrentSession()
        .createQuery("SELECT r FROM Ring r " +
            "WHERE r.radar.id = :radarId " +
            "ORDER BY r.position", Ring.class)
        .setParameter("radarId", filter.getRadarId())
        .getResultList();
  }

  public Optional<Ring> findById(Long id) {
    return sessionFactory.getCurrentSession()
        .createQuery("SELECT r FROM Ring r WHERE r.id = :id", Ring.class)
        .setParameter("id", id)
        .uniqueResultOptional();
  }
}

