package ru.hh.techradar.repository;

import jakarta.inject.Inject;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.hh.techradar.entity.Company;

@Repository
public class CompanyRepository extends BaseRepositoryImpl<Company> {

  private final SessionFactory sessionFactory;

  @Inject
  public CompanyRepository(SessionFactory sessionFactory) {
    super(sessionFactory, Company.class);
    this.sessionFactory = sessionFactory;
  }
}
