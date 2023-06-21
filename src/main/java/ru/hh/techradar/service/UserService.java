package ru.hh.techradar.service;

import jakarta.validation.Valid;
import jakarta.validation.Validator;
import java.util.Collection;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.hh.techradar.entity.Company;
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

  private final Validator validator;

  public UserService(
      UserRepository userRepository,
      UserMapper userMapper,
      CompanyService companyService, Validator validator) {
    this.userRepository = userRepository;
    this.userMapper = userMapper;
    this.companyService = companyService;
    this.validator = validator;
  }

  @Transactional(readOnly = true)
  public Collection<User> findAllByFilter(@Valid UserFilter filter) {
    return companyService.findUsersByCompanyId(filter.getCompanyId());
  }

  @Transactional(readOnly = true)
  public User findById(Long id) {
    return userRepository.findById(id).orElseThrow(() -> new NotFoundException(User.class, id));
  }

  @Transactional(readOnly = true)
  public User findByUsername(String username) {
    return userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException(User.class, username));
  }

  @Transactional
  public void deleteById(Long id) {
    userRepository.deleteById(id);
  }

  @Transactional
  public User update(Long id, User user) {
    User found = userRepository.findById(id).orElseThrow(() -> new NotFoundException(User.class, id));
    if (!user.equals(found) && isPresent(user)) {
      throw new EntityExistsException(User.class, user.getUsername());
    }
    user = userMapper.toUpdate(found, user);
    validator.validate(user);
    return userRepository.update(user);
  }

  @Transactional
  public User save(@Valid User entity) {
    if (isPresent(entity)) {
      throw new EntityExistsException(User.class, entity.getUsername());
    }
    return userRepository.save(entity);
  }

  private boolean isPresent(@Valid User entity) {
    return userRepository.findByUsername(entity.getUsername()).isPresent();
  }

  @Transactional
  public void joinUserAndCompany(String username, Long companyId) {
    User user = findByUsername(username);
    Company company = companyService.findById(companyId);
    user.addCompany(company);
  }

  @Transactional
  public void disjointUserAndCompany(String username, Long companyId) {
    User user = findByUsername(username);
    Company company = companyService.findById(companyId);
    user.removeCompany(company);
  }

  @Transactional(readOnly = true)
  public Collection<Company> findAllCompaniesByUserId(Long userId) {
    User user = findById(userId);
    Hibernate.initialize(user.getCompanies());
    return user.getCompanies();
  }

  @Transactional(readOnly = true)
  public Collection<Company> findAllCompaniesByUsername(String username) {
    User user = findByUsername(username);
    return findAllCompaniesByUserId(user.getId());
  }
}
