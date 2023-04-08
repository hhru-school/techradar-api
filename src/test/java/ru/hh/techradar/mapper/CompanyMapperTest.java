package ru.hh.techradar.mapper;

import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.hh.techradar.dto.CompanyDto;
import ru.hh.techradar.entity.Company;

class CompanyMapperTest {

  private final CompanyMapper companyMapper = new CompanyMapper();
  List<Company> companies;
  List<CompanyDto> companyDtos;


  @BeforeEach
  void setUp() {
    companies = List.of(
        new Company(1L, "name1"),
        new Company(2L, "name2"),
        new Company(3L, "name3")
    );

    companyDtos = List.of(
        new CompanyDto(1L, "name1"),
        new CompanyDto(2L, "name2"),
        new CompanyDto(3L, "name3")
    );

  }

  @Test
  void toDtos() {
    List<CompanyDto> actual = companyMapper.toDtos(companies);
    assertEquals(actual, companyDtos);
  }

  @Test
  void toEntities() {
    List<Company> actual = companyMapper.toEntities(companyDtos);
    assertEquals(actual, companies);
  }
}
