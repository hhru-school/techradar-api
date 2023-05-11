package ru.hh.techradar.repository;

import java.util.Collection;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.hh.techradar.entity.Blip;
import ru.hh.techradar.entity.Radar;
import ru.hh.techradar.entity.RadarVersion;

@Repository
public class RadarVersionRepository extends BaseRepositoryImpl<Long, RadarVersion> {
  private final SessionFactory sessionFactory;

  public RadarVersionRepository(SessionFactory sessionFactory) {
    super(sessionFactory, RadarVersion.class);
    this.sessionFactory = sessionFactory;
  }

  public Collection<RadarVersion> findAllByRadarId(Long radarId) {
    Session session = sessionFactory.openSession();
    return session.createQuery("SELECT rv FROM RadarVersion rv WHERE rv.radar.id = :radarId", RadarVersion.class)
        .setParameter("radarId", radarId)
        .getResultList();
  }
}
