package ru.hh.techradar.dao;

import jakarta.inject.Inject;
import java.util.Optional;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.hh.techradar.entity.Company;

@Repository
public class CompanyDao {

  private final SessionFactory sessionFactory;

  @Inject
  public CompanyDao(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }


  public Optional<Company> findById(Long id) {
    return Optional.ofNullable(sessionFactory.getCurrentSession().get(Company.class, id));
  }

  public Company update(Company company) {
    return sessionFactory.getCurrentSession().merge(company);
  }

  public Company save(Company company) {
    sessionFactory.getCurrentSession().persist(company);
    return company;
  }


  public void deleteById(Long id) {
    sessionFactory.getCurrentSession()
        .createQuery("DELETE Company c WHERE c.id = :id", Company.class)
        .setParameter("id", id)
        .executeUpdate();
  }

}
