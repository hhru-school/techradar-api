package ru.hh.techradar.service;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hh.techradar.entity.User;
import ru.hh.techradar.exception.EntityExistsException;
import ru.hh.techradar.exception.NotFoundException;
import ru.hh.techradar.filter.UserFilter;
import ru.hh.techradar.mapper.UserMapper;
import ru.hh.techradar.repository.UserRepository;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final CompanyService companyService;

  public UserService(
      UserRepository userRepository,
      UserMapper userMapper,
      CompanyService companyService) {
    this.userRepository = userRepository;
    this.userMapper = userMapper;
    this.companyService = companyService;
  }

  @Transactional(readOnly = true)
  public List<User> findAllByFilter(UserFilter filter) {
    return userRepository.findAllByFilter(filter);
  }

  @Transactional(readOnly = true)
  public User findById(Long id) {
    return userRepository.findById(id).orElseThrow(() -> new NotFoundException(User.class, id));
  }

  @Transactional
  public void deleteById(Long id) {
    userRepository.deleteById(id);
  }

  @Transactional
  public User update(Long id, Long companyId, User user) {
    User found = userRepository.findById(id).orElseThrow(() -> new NotFoundException(User.class, id));
    if (!user.equals(found) && isPresent(user)) {
      throw new EntityExistsException(User.class, user.getUsername());
    }
    if (Objects.nonNull(companyId)) {
      found.setCompany(companyService.findById(companyId));
    }
    found.setLastChangeTime(Instant.now());
    return userRepository.update(userMapper.toUpdate(found, user));
  }

  @Transactional
  public User save(Long companyId, User entity) {
    if (isPresent(entity)) {
      throw new EntityExistsException(User.class, entity.getUsername());
    }
    entity.setCompany(companyService.findById(companyId));
    return userRepository.save(entity);
  }

  private boolean isPresent(User entity) {
    return userRepository.findByUsername(entity.getUsername()).isPresent();
  }
}
