package ru.hh.techradar.service;

import jakarta.inject.Inject;
import java.util.Collection;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hh.techradar.entity.Company;
import ru.hh.techradar.entity.User;
import ru.hh.techradar.exception.NotFoundException;
import ru.hh.techradar.mapper.CompanyMapper;
import ru.hh.techradar.repository.CompanyRepository;

@Service
public class CompanyService implements BaseService<Long, Company> {

  private final CompanyRepository companyRepository;
  private final CompanyMapper companyMapper;
  private final Validator validator;

  @Inject
  public CompanyService(
      CompanyRepository companyRepository,
      CompanyMapper companyMapper,
      Validator validator) {
    this.companyRepository = companyRepository;
    this.companyMapper = companyMapper;
    this.validator = validator;
  }

  @Override
  @Transactional(readOnly = true)
  public Company findById(Long id) {
    return companyRepository.findById(id).orElseThrow(() -> new NotFoundException(Company.class, id));
  }

  @Override
  @Transactional
  public void deleteById(Long id) {
    companyRepository.deleteById(id);
  }

  @Override
  @Transactional
  public Company update(Long id, Company company) {
    Company found = companyRepository.findById(id).orElseThrow(() -> new NotFoundException(Company.class, id));
    company = companyMapper.toUpdate(found, company);
    validator.validate(company);
    return companyRepository.update(company);
  }

  @Override
  @Transactional
  public Company save(@Valid Company company) {
    return companyRepository.save(company);
  }

  @Override
  @Transactional(readOnly = true)
  public List<Company> findAll() {
    return companyRepository.findAll();
  }

  @Transactional(readOnly = true)
  public List<User> findUsersByCompanyId(Long companyId) {
    return companyRepository.findUsersByCompanyId(companyId);
  }

  @Transactional(readOnly = true)
  public Collection<Company> findAllCompaniesWithRadars() {
    return companyRepository.findAllCompaniesWithRadars();
  }
}
