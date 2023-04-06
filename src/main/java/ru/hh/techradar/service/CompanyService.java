package ru.hh.techradar.service;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import ru.hh.techradar.entity.Company;
import ru.hh.techradar.exception.NotFoundException;
import ru.hh.techradar.mapper.CompanyMapper;
import ru.hh.techradar.repository.CompanyRepository;

@org.springframework.stereotype.Service
public class CompanyService implements Service<Company> {

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
  @Transactional
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
    return companyRepository.update(companyMapper.toUpdate(found, company));
  }

  @Override
  @Transactional
  public Company save(Company company) {
    return companyRepository.save(company);
  }
}
