package ru.hh.techradar.repository;

import jakarta.inject.Inject;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.hh.techradar.entity.Ring;

@Repository
public class RingRepository extends BaseRepositoryImpl<Long, Ring> {
  @Inject
  public RingRepository(SessionFactory sessionFactory) {
    super(sessionFactory, Ring.class);
  }
}

