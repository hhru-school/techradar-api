package ru.hh.techradar.repository;

import java.util.Collection;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.springframework.stereotype.Repository;
import ru.hh.techradar.entity.Radar;

@Repository
public class VersionRepository {

  private final SessionFactory sessionFactory;

  public VersionRepository(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

//  public Radar findRevisionOfRadarById(Long radarId, Long revisionId) {
//    Session currentSession = sessionFactory.getCurrentSession();
//    AuditReader auditReader = AuditReaderFactory.get(currentSession);
//    List<Number> revisionNumbers = auditReader.getRevisions(Radar.class, radarId);
//    Radar radar = new Radar();
//    for (Number rev : revisionNumbers) {
//      radar = auditReader.find(Radar.class, radarId, rev);
//
//    }
//    return radar;
//  }

  public Radar findRevisionOfRadarById(Long radarId, Long revisionId) {
    Session currentSession = sessionFactory.getCurrentSession();
    AuditReader auditReader = AuditReaderFactory.get(currentSession);
    Radar radar = auditReader.find(Radar.class, radarId, revisionId);
    return radar;
  }

//  public Radar findRevisionOfRadarById(Long radarId, Long revisionId) {
//    Session currentSession = sessionFactory.getCurrentSession();
//    AuditReader auditReader = AuditReaderFactory.get(currentSession);
//    Number singleResult = (Number) auditReader.createQuery()
//        .forRevisionsOfEntity(Radar.class, false, true)
//        .addProjection(AuditEntity.revisionNumber().max())
//        .add(AuditEntity.id().eq(radarId))
//        .add(AuditEntity.revisionNumber().lt(revisionId))
//        .getSingleResult();
//    return auditReader.find(Radar.class, revisionId, singleResult);
//  }

  public Collection<Radar> findAllByFilter(Long radarId) {
    Session currentSession = sessionFactory.getCurrentSession();
    List<Radar> resultList = AuditReaderFactory.get(currentSession)
        .createQuery()
        .forRevisionsOfEntity(Radar.class, true, true)
        .add(AuditEntity.id().eq(radarId))
        .getResultList();
//    System.out.println(resultList);
    return resultList;
  }
}
