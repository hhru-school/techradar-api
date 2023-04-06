package ru.hh.techradar.repository;

import jakarta.inject.Inject;
import java.util.Objects;
import java.util.Optional;
import org.hibernate.SessionFactory;
import ru.hh.techradar.entity.Company;

@org.springframework.stereotype.Repository
public class CompanyRepository implements Repository<Company> {

  private final SessionFactory sessionFactory;

  @Inject
  public CompanyRepository(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  @Override
  public Optional<Company> findById(Long id) {
    return Optional.ofNullable(sessionFactory.getCurrentSession().get(Company.class, id));
  }

  @Override
  public Company update(Company company) {
    return sessionFactory.getCurrentSession().merge(company);
  }

  @Override
  public Company save(Company company) {
    sessionFactory.getCurrentSession().persist(company);
    return company;
  }

  @Override
  public void delete(Company entity) {
    sessionFactory.getCurrentSession().remove(entity);
  }

  @Override
  public void deleteById(Long id) {
    Company company = sessionFactory.getCurrentSession().get(Company.class, id);
    if (Objects.nonNull(company)) {
      sessionFactory.getCurrentSession().remove(company);
    }
  }
}
