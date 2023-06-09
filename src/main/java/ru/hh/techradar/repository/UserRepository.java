package ru.hh.techradar.repository;

import java.util.Optional;
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

  public Optional<User> findByUsername(String username) {
    return sessionFactory.getCurrentSession()
        .createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
        .setParameter("username", username)
        .uniqueResultOptional();
  }
}
