package ru.hh.techradar.repository;

import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.hh.techradar.entity.Company;
import ru.hh.techradar.entity.User;

@Repository
public class CompanyRepository extends BaseRepositoryImpl<Long, Company> {

  private final SessionFactory sessionFactory;

  public CompanyRepository(SessionFactory sessionFactory) {
    super(sessionFactory, Company.class);
    this.sessionFactory = sessionFactory;
  }

  public List<User> findUsersByCompany(Long companyId) {
    return sessionFactory.getCurrentSession()
        .createQuery("""
            SELECT c.users FROM Company c WHERE c.id = :companyId
            """, User.class)
        .setParameter("companyId", companyId)
        .getResultList();
  }
}
