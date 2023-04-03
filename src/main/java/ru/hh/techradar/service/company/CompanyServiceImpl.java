package ru.hh.techradar.service.company;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import ru.hh.techradar.dao.CompanyDao;
import ru.hh.techradar.entity.Company;
import ru.hh.techradar.exception.NotFoundException;
import ru.hh.techradar.mapper.CompanyMapper;

@Service
public class CompanyServiceImpl implements CompanyService {

  private final CompanyDao companyDao;
  private final CompanyMapper companyMapper;

  @Inject
  public CompanyServiceImpl(CompanyDao companyDao,
                            CompanyMapper companyMapper) {
    this.companyDao = companyDao;
    this.companyMapper = companyMapper;
  }

  @Override
  @Transactional
  public Company findById(Long id) {
    return companyDao.findById(id).orElseThrow(() -> new NotFoundException(Company.class, id));
  }

  @Override
  @Transactional
  public void deleteById(Long id) {
    companyDao.deleteById(id);
  }


  @Override
  @Transactional
  public Company update(Long id, Company company) {
    Company found = companyDao.findById(id).orElseThrow(() -> new NotFoundException(Company.class, id));
    return companyDao.update(companyMapper.toCompanyUpdate(found, company));
  }

  @Override
  @Transactional
  public Company save(Company company) {
    return companyDao.save(company);
  }

}
