package ru.hh.techradar.repository;

import jakarta.inject.Inject;
import java.util.List;
import java.util.Optional;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.hh.techradar.entity.Blip;
import ru.hh.techradar.entity.Ring;
import ru.hh.techradar.filter.ComponentFilter;

@Repository
public class RingRepository extends BaseRepositoryImpl<Long, Ring> {
  private final SessionFactory sessionFactory;
  private final BlipRepository blipRepository;

  @Inject
  public RingRepository(SessionFactory sessionFactory, BlipRepository blipRepository) {
    super(sessionFactory, Ring.class);
    this.sessionFactory = sessionFactory;
    this.blipRepository = blipRepository;
  }

  public List<Ring> findAllByFilter(ComponentFilter filter) {
    return sessionFactory.getCurrentSession()
        .createQuery("SELECT r FROM Ring r " +
                "LEFT JOIN FETCH r.settings rs " +
                "WHERE r.radar.id = :radarId " +
                "AND r.creationTime <= :actualDate " +
                "AND (r.removedAt IS NULL OR r.removedAt > :actualDate) " +
                "AND rs.creationTime IN(SELECT max(rs2.creationTime) FROM RingSetting rs2 WHERE rs2.ring.id = r.id " +
                "AND rs2.creationTime <= :actualDate) " +
                "ORDER BY rs.position"
            , Ring.class)
        .setParameter("radarId", filter.getRadarId())
        .setParameter("actualDate", filter.getActualDate())
        .getResultList();
  }

  public Optional<Ring> findById(Long id) {
    return sessionFactory.getCurrentSession()
        .createQuery("SELECT r FROM Ring r LEFT JOIN FETCH r.settings s WHERE r.id = :id", Ring.class)
        .setParameter("id", id)
        .uniqueResultOptional();
  }

  public Boolean isContainBlipsById(Long id) {
//    var session = sessionFactory.openSession();
//    session.createQuery(
//            "SELECT b FROM Blip b " +
//                "LEFT JOIN b.blipEvents be " +
//                "INNER JOIN be.ring r " +
//                "WHERE be.creationTime IN (SELECT MAX(c.creationTime) FROM BlipEvent c " +
//                "WHERE c.blip.id = b.id) AND r.id = :ringId", Blip.class)
//        .setParameter("ringId", id)
//        .getResultStream().forEach(System.out::println);
    return sessionFactory.getCurrentSession().createQuery(
            "SELECT COUNT(b) FROM Blip b " +
                "LEFT JOIN b.blipEvents be " +
                "INNER JOIN be.ring r " +
                "WHERE be.blip.id = b.id AND r.id = :ringId", Long.class)
        .setParameter("ringId", id)
        .getSingleResult() > 0;
  }

  public void forceRemoveById(Long id) {
    sessionFactory.getCurrentSession().createQuery(
            "SELECT b FROM Blip b " +
                "LEFT JOIN b.blipEvents be " +
                "INNER JOIN be.ring r " +
                "WHERE be.blip.id = b.id AND r.id = :ringId", Blip.class)
        .setParameter("ringId", id)
        .getResultStream().forEach(blipRepository::delete);
    deleteById(id);
  }
}

