package ru.hh.techradar.service;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hh.techradar.entity.Company;
import ru.hh.techradar.entity.User;
import ru.hh.techradar.exception.NotFoundException;
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
      CompanyService companyService
  ) {
    this.userRepository = userRepository;
    this.userMapper = userMapper;
    this.companyService = companyService;
  }

  @Transactional(readOnly = true)
  public List<User> findAllByFilter(Long companyId) {
    return userRepository.findAllByFilter(companyId);
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
  public User save(Long companyId, User entity) {
    Company company = companyService.findById(companyId);
    entity.setCompany(company);
    return userRepository.save(entity);
  }

  @Transactional
  public User update(Long id, Long companyId, User user) {
    User found = userRepository.findById(id).orElseThrow(() -> new NotFoundException(User.class, id));
    found.setLastChangeTime(Instant.now());
    if (Objects.nonNull(companyId)) {
      found.setCompany(companyService.findById(companyId));
    }
    return userRepository.update(userMapper.toUpdate(found, user));
  }
}
