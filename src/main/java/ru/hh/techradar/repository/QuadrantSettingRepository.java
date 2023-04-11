package ru.hh.techradar.repository;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.hh.techradar.entity.QuadrantSetting;

@Repository
public class QuadrantSettingRepository extends BaseRepositoryImpl<Long, QuadrantSetting> {

  private final SessionFactory sessionFactory;

  public QuadrantSettingRepository(SessionFactory sessionFactory) {
    super(sessionFactory, QuadrantSetting.class);
    this.sessionFactory = sessionFactory;
  }
}

