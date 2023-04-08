package ru.hh.techradar.mapper;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Component;
import ru.hh.techradar.dto.CompanyDto;
import ru.hh.techradar.entity.Company;

@Component
public class CompanyMapper implements BaseMapper<Company, CompanyDto> {

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

  @Override
  public List<CompanyDto> toDtos(Collection<Company> entities) {
    return entities.stream().map(this::toDto).toList();
  }

  @Override
  public List<Company> toEntities(Collection<CompanyDto> dtos) {
    return dtos.stream().map(this::toEntity).toList();
  }

  @Override
  public Company toUpdate(Company target, Company source) {
    if (Objects.nonNull(source.getName())) {
      target.setName(source.getName());
    }
    return target;
  }
}
