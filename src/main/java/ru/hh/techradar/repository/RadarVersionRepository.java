package ru.hh.techradar.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.hh.techradar.entity.BlipEvent;
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

  public Optional<RadarVersion> findLastReleasedRadarVersion(Long radarId) {
    Session session = sessionFactory.openSession();
    return session.createQuery("""
            SELECT rv FROM RadarVersion rv WHERE rv.radar.id = :radarId AND rv.release AND rv.level =
            (SELECT MAX(rv2.level) from RadarVersion rv2 WHERE rv2.radar.id = :radarId AND rv2.release)
            """, RadarVersion.class
        ).setParameter("radarId", radarId)
        .uniqueResultOptional();
  }

  public Boolean checkIfReleasedVersionOnLevel(Integer level, Long radarId) {
    Session session = sessionFactory.openSession();
    List<RadarVersion> radarVersions = session.createQuery("""
            SELECT rv FROM RadarVersion rv
            WHERE rv.radar.id = :radarId
            AND rv.level = :level
            AND rv.release = true
            """, RadarVersion.class)
        .setParameter("level", level)
        .setParameter("radarId", radarId)
        .getResultList();
    return !radarVersions.isEmpty();
  }

  public List<RadarVersion> findChildrenWithSameBlipEventId(RadarVersion notYetModifiedRadarVersion) {
    Session session = sessionFactory.openSession();
    return session.createQuery("""
            SELECT rv FROM RadarVersion rv
            WHERE rv.parent.id = :parentId
            AND rv.blipEvent.id = :blipEventId
            """, RadarVersion.class)
        .setParameter("parentId", notYetModifiedRadarVersion.getId())
        .setParameter("blipEventId", notYetModifiedRadarVersion.getBlipEvent().getId())
        .getResultList();
  }

  public List<RadarVersion> findRadarVersionsWithCertainBlipEvent(BlipEvent blipEvent) {
    Session session = sessionFactory.openSession();
    return session.createQuery("""
            SELECT rv FROM RadarVersion rv
            WHERE rv.blipEvent.id = :blipEventId
            """, RadarVersion.class)
        .setParameter("blipEventId", blipEvent.getId())
        .getResultList();
  }

  public void setBlipEventToChildrenWhereBlipEventEqualsToParents(RadarVersion notYetModifiedRadarVersion, BlipEvent blipEventToSet) {
    List<RadarVersion> resultList = findChildrenWithSameBlipEventId(notYetModifiedRadarVersion);
    resultList.forEach(rv -> {
      rv.setBlipEvent(blipEventToSet);
      update(rv);
    });
  }

  public void setToggleAvailabilityOfRelatives(Long parentId, Boolean toggleAvailability) {
    Session session = sessionFactory.openSession();
    List<RadarVersion> resultList = session.createQuery("""
            SELECT rv FROM RadarVersion rv WHERE rv.parent.id = :parentId
            """, RadarVersion.class)
        .setParameter("parentId", parentId)
        .getResultList();
    resultList.forEach(rv -> {
      rv.setToggleAvailable(toggleAvailability);
      update(rv);
    });
  }

  public void setToggleAvailability(Long radarVersionId, Boolean toggleAvailability) {
    Session session = sessionFactory.openSession();
    RadarVersion result = session.createQuery("""
            SELECT rv FROM RadarVersion rv WHERE rv.id = :radarVersionId
            """, RadarVersion.class)
        .setParameter("radarVersionId", radarVersionId)
        .getSingleResult();
    result.setToggleAvailable(toggleAvailability);
    update(result);
  }

  public Optional<RadarVersion> findByNameAndRadarId(String name, Long radarId) {
    Session session = sessionFactory.openSession();
    return session.createQuery("SELECT rv FROM RadarVersion rv WHERE rv.radar.id = :radarId AND rv.name = :name", RadarVersion.class)
        .setParameter("name", name)
        .setParameter("radarId", radarId)
        .uniqueResultOptional();
  }

  public List<RadarVersion> findChildren(Long id) {
    Session session = sessionFactory.openSession();
    return session.createQuery("""
            SELECT rv FROM RadarVersion rv WHERE rv.parent.id = :parentId
            """, RadarVersion.class)
        .setParameter("parentId", id)
        .getResultList();
  }


  public Optional<RadarVersion> findRoot(Long radarId) {
    Session session = sessionFactory.openSession();
    return session.createQuery("""
            SELECT rv FROM RadarVersion rv WHERE rv.radar.id = :radarId AND rv.parent IS NULL
            """, RadarVersion.class
        ).setParameter("radarId", radarId)
        .uniqueResultOptional();
  }

  public void replaceBlipEventByItsParent(BlipEvent blipEvent, BlipEvent parentBlipEvent) {
    List<RadarVersion> resultList = findRadarVersionsWithCertainBlipEvent(blipEvent);
    resultList.forEach(rv -> {
      rv.setBlipEvent(parentBlipEvent);
      update(rv);
    });
  }

  public Collection<RadarVersion> findAllReleasedRadarVersions(Long radarId) {
    Session session = sessionFactory.openSession();
    return session.createQuery("SELECT rv FROM RadarVersion rv WHERE rv.radar.id = :radarId AND rv.release = true", RadarVersion.class)
        .setParameter("radarId", radarId)
        .getResultList();
  }
}
