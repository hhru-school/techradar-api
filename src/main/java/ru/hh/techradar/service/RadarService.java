package ru.hh.techradar.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hh.techradar.dto.RadarDto;
import ru.hh.techradar.entity.Radar;
import ru.hh.techradar.exception.NotFoundException;
import ru.hh.techradar.mapper.RadarMapper;
import ru.hh.techradar.repository.RadarRepository;

@Service
public class RadarService {
  private final RadarRepository radarRepository;
  private final CompanyService companyService;
  private final RadarMapper radarMapper;
  private final UserService userService;


  public RadarService(
      RadarRepository radarRepository,
      CompanyService companyService,
      RadarMapper radarMapper,
      UserService userService
      ) {
    this.radarRepository = radarRepository;
    this.companyService = companyService;
    this.radarMapper = radarMapper;
    this.userService = userService;
  }

  @Transactional
  public Radar save(RadarDto dto) {
    Radar radar = radarMapper.toEntity(dto);
    radar.setAuthor(userService.findById(dto.getAuthorId()));
    radar.setCompany(companyService.findById(dto.getCompanyId()));
    return radarRepository.save(radar);
  }

  @Transactional(readOnly = true)
  public Radar findById(Long id) {
    return radarRepository.findById(id).orElseThrow(() -> new NotFoundException(Radar.class, id));
  }

  @Transactional(readOnly = true)
  public List<Radar> findAllByFilter(Long companyId, Instant actualDate) {
    companyService.findById(companyId);
    actualDate = Optional.ofNullable(actualDate).orElse(Instant.now());
    return radarRepository.findAllByFilter(companyId, actualDate);
  }

  @Transactional
  public Radar update(Long id, Radar radar) {
    Radar found = radarRepository.findById(id).orElseThrow(() -> new NotFoundException(Radar.class, id));
    found.setLastChangeTime(Instant.now());
    return radarRepository.update(radarMapper.toUpdate(found, radar));
  }

  @Transactional
  public void deleteById(Long id) {
    radarRepository.deleteById(id);
  }
}
