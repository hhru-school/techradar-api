package ru.hh.techradar.service;

import jakarta.inject.Inject;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hh.techradar.entity.Company;
import ru.hh.techradar.exception.NotFoundException;
import ru.hh.techradar.mapper.CompanyMapper;
import ru.hh.techradar.repository.CompanyRepository;

@Service
public class CompanyService implements BaseService<Long, Company> {

  private final CompanyRepository companyRepository;
  private final CompanyMapper companyMapper;

  @Inject
  public CompanyService(
      CompanyRepository companyRepository,
      CompanyMapper companyMapper
  ) {
    this.companyRepository = companyRepository;
    this.companyMapper = companyMapper;
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
    found.setLastChangeTime(Instant.now());
    return companyRepository.update(companyMapper.toUpdate(found, company));
  }

  @Override
  @Transactional
  public Company save(Company company) {
    return companyRepository.save(company);
  }

  @Override
  @Transactional(readOnly = true)
  public List<Company> findAll() {
    return companyRepository.findAll();
  }
}
