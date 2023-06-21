package ru.hh.techradar.service;

import jakarta.validation.Validator;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hh.techradar.dto.RadarDto;
import ru.hh.techradar.entity.Radar;
import ru.hh.techradar.exception.EntityExistsException;
import ru.hh.techradar.exception.NotFoundException;
import ru.hh.techradar.mapper.RadarMapper;
import ru.hh.techradar.repository.RadarRepository;

@Service
public class RadarService {
  private final RadarRepository radarRepository;
  private final CompanyService companyService;
  private final RadarMapper radarMapper;
  private final UserService userService;
  private final Validator validator;


  public RadarService(
      RadarRepository radarRepository,
      CompanyService companyService,
      RadarMapper radarMapper,
      UserService userService,
      Validator validator) {
    this.radarRepository = radarRepository;
    this.companyService = companyService;
    this.radarMapper = radarMapper;
    this.userService = userService;
    this.validator = validator;
  }

  @Transactional
  public Radar save(RadarDto dto, String username) {
    if(radarRepository.findByNameAndCompanyId(dto.getName(), dto.getCompanyId()).isPresent()) {
      throw new EntityExistsException(Radar.class, dto.getName());
    }
    Radar radar = radarMapper.toEntity(dto);
    radar.setAuthor(userService.findByUsername(username));
    radar.setCompany(companyService.findById(dto.getCompanyId()));
    validator.validate(radar);
    return radarRepository.save(radar);
  }

  @Transactional(readOnly = true)
  public Radar findById(Long id) {
    return radarRepository.findById(id).orElseThrow(() -> new NotFoundException(Radar.class, id));
  }

  @Transactional(readOnly = true)
  public List<Radar> findAllByCompanyId(Long companyId) {
    return radarRepository.findAllByCompanyId(companyId);
  }

  @Transactional
  public Radar update(Long id, Radar radar) {
    Radar found = radarRepository.findById(id).orElseThrow(() -> new NotFoundException(Radar.class, id));
    if (radar.getName() != null && !found.getName().equals(radar.getName()) && radarRepository.findByNameAndCompanyId(
        radar.getName(),
        found.getCompany().getId()
    ).isPresent()) {
      throw new EntityExistsException(Radar.class, radar.getName());
    }
    radar = radarMapper.toUpdate(found, radar);
    validator.validate(radar);
    return radarRepository.update(radar);
  }

  @Transactional
  public void deleteById(Long id) {
    radarRepository.deleteById(id);
  }
}
