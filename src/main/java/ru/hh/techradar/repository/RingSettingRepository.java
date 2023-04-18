package ru.hh.techradar.repository;

import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.Optional;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.hh.techradar.entity.Ring;
import ru.hh.techradar.entity.RingSetting;

@Repository
public class RingSettingRepository extends BaseRepositoryImpl<Long, RingSetting> {
  private final SessionFactory sessionFactory;

  public RingSettingRepository(SessionFactory sessionFactory) {
    super(sessionFactory, RingSetting.class);
    this.sessionFactory = sessionFactory;
  }

  @Transactional
  public Optional<RingSetting> findRingSettingByDate(Ring ring, Instant date) {
    // todo check
    return sessionFactory.getCurrentSession().createQuery(
            "SELECT rs FROM RingSetting rs " +
                "WHERE rs.ring = :ring AND :date >= rs.creationTime ORDER BY rs.creationTime DESC LIMIT 1", RingSetting.class
        )
        .setParameter("ring", ring)
        .setParameter("date", date)
        .uniqueResultOptional();
  }
}
