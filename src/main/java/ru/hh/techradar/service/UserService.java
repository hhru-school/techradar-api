package ru.hh.techradar.service;

import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Service;
import ru.hh.techradar.entity.User;
import ru.hh.techradar.exception.NotFoundException;
import ru.hh.techradar.repository.UserRepository;

@Service
public class UserService implements BaseService<Long, User> {

  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  @Transactional
  public User findById(Long id) {
    return userRepository.findById(id).orElseThrow(() -> new NotFoundException(User.class, id));
  }

  @Override
  @Transactional
  public void deleteById(Long id) {
    userRepository.deleteById(id);
  }

  @Override
  @Transactional
  public User update(Long id, User entity) {
    User found = userRepository.findById(id).orElseThrow(() -> new NotFoundException(User.class, id));
//    return radarRepository.update(companyMapper.toUpdate(found, entity));
    return null;
  }

  @Override
  @Transactional
  public User save(User entity) {
    return userRepository.save(entity);
  }

  @Override
  @Transactional
  public List<User> findAll() {
    return userRepository.findAll();
  }
}
