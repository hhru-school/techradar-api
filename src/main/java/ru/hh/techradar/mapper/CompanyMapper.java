package ru.hh.techradar.mapper;

import java.util.Objects;
import org.springframework.stereotype.Component;
import ru.hh.techradar.dto.CompanyDto;
import ru.hh.techradar.entity.Company;

@Component
public class CompanyMapper extends AbstractMapper<Company, CompanyDto> {

  @Override
  public Company toEntity(CompanyDto dto) {
    Company company = new Company();
    company.setId(dto.getId());
    company.setName(dto.getName());
    return company;
  }

  @Override
  public CompanyDto toDto(Company entity) {
    CompanyDto companyDto = new CompanyDto();
    companyDto.setId(entity.getId());
    companyDto.setName(entity.getName());
    return companyDto;
  }

  public Company toUpdate(Company target, Company source) {
    if (Objects.nonNull(source.getName())) {
      target.setName(source.getName());
    }
    return target;
  }
}
