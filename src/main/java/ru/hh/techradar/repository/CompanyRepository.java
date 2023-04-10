package ru.hh.techradar.repository;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.hh.techradar.entity.Company;

@Repository
public class CompanyRepository extends BaseRepositoryImpl<Long, Company> {

  private final SessionFactory sessionFactory;

  public CompanyRepository(SessionFactory sessionFactory) {
    super(sessionFactory, Company.class);
    this.sessionFactory = sessionFactory;
  }
}
