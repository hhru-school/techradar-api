package ru.hh.techradar.mapper;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Component;
import ru.hh.techradar.dto.CompanyDto;
import ru.hh.techradar.entity.Company;

@Component
public class CompanyMapper {

  public Company toCompany(CompanyDto companyDto) {
    Company company = new Company();
    company.setId(company.getId());
    company.setName(companyDto.getName());
    return company;
  }

  public CompanyDto toCompanyDto(Company company) {
    CompanyDto companyDto = new CompanyDto();
    companyDto.setId(company.getId());
    companyDto.setName(company.getName());
    return companyDto;
  }

  public Company toCompanyUpdate(Company found, Company update) {
    if (Objects.nonNull(update.getName())) {
      found.setName(update.getName());
    }
    return found;
  }

  public List<CompanyDto> toCompanyDtoList(Collection<Company> companies) {
    return companies.stream().map(this::toCompanyDto).toList();
  }
}