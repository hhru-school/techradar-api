package ru.hh.techradar.service.company;

import ru.hh.techradar.entity.Company;

public interface CompanyService {

  Company findById(Long id);

  void deleteById(Long id);

  Company update(Long id, Company company);

  Company save(Company company);

}
