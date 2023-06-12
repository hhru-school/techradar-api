package ru.hh.techradar.repository;

import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.hh.techradar.entity.BlipTemplate;

@Repository
public class BlipTemplateRepository extends BaseRepositoryImpl<String, BlipTemplate> {
  private final SessionFactory sessionFactory;

  public BlipTemplateRepository(SessionFactory sessionFactory) {
    super(sessionFactory, BlipTemplate.class);
    this.sessionFactory = sessionFactory;
  }

  public List<BlipTemplate> findByNamePart(String namePart) {
    return sessionFactory.getCurrentSession().createQuery(
            "SELECT bt FROM BlipTemplate bt WHERE bt.name LIKE :namePart", BlipTemplate.class
        )
        .setParameter("namePart", '%' + namePart + '%')
        .getResultList();
  }
}
