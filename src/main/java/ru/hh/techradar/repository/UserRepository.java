package ru.hh.techradar.repository;

import java.util.List;
import java.util.Optional;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.hh.techradar.entity.User;
import ru.hh.techradar.filter.UserFilter;

@Repository
public class UserRepository extends BaseRepositoryImpl<Long, User> {
  private final SessionFactory sessionFactory;

  public UserRepository(SessionFactory sessionFactory) {
    super(sessionFactory, User.class);
    this.sessionFactory = sessionFactory;
  }

  public List<User> findAllByFilter(UserFilter filter) {
    return sessionFactory.getCurrentSession()
//        .createQuery("SELECT u FROM User u WHERE u.company.id =: companyId ORDER BY u.id", User.class)
        .createQuery("", User.class)
        .setParameter("companyId", filter.getCompanyId())
        .getResultList();
  }

  public Optional<User> findByUsername(String username) {
    return sessionFactory.getCurrentSession()
        .createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
        .setParameter("username", username)
        .uniqueResultOptional();
  }

//  public void addCompany(User user, Company company) {
//    user.addCompany(company);
//  }
}
