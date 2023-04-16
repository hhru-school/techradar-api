package ru.hh.techradar.repository;

import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.hh.techradar.entity.User;

@Repository
public class UserRepository extends BaseRepositoryImpl<Long, User> {

  private final SessionFactory sessionFactory;

  public UserRepository(SessionFactory sessionFactory) {
    super(sessionFactory, User.class);
    this.sessionFactory = sessionFactory;
  }

  public List<User> findAllByFilter(Long companyId) {
    return sessionFactory.getCurrentSession()
        .createQuery("SELECT u FROM User u WHERE u.company.id =: companyId ORDER BY u.id ", User.class)
        .setParameter("companyId", companyId)
        .getResultList();
  }
}
