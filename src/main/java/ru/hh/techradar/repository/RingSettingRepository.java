package ru.hh.techradar.repository;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.hh.techradar.entity.RingSetting;

@Repository
public class RingSettingRepository extends BaseRepositoryImpl<Long, RingSetting> {

  private final SessionFactory sessionFactory;

  public RingSettingRepository(SessionFactory sessionFactory) {
    super(sessionFactory, RingSetting.class);
    this.sessionFactory = sessionFactory;
  }
}
