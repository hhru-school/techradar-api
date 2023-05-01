package ru.hh.techradar.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hh.techradar.entity.Radar;
import ru.hh.techradar.exception.NotFoundException;
import ru.hh.techradar.repository.RadarRepository;

@Service
public class RadarService {
  private final RadarRepository radarRepository;
  private final CompanyService companyService;

  public RadarService(
      RadarRepository radarRepository,
      CompanyService companyService) {
    this.radarRepository = radarRepository;
    this.companyService = companyService;
  }

  @Transactional(readOnly = true)
  public List<Radar> findAllByFilter(Long companyId, Instant actualDate) {
    companyService.findById(companyId);
    actualDate = Optional.ofNullable(actualDate).orElse(Instant.now());
    return radarRepository.findAllByFilter(companyId, actualDate);
  }

  @Transactional(readOnly = true)
  public Radar findById(Long id) {
    return radarRepository.findById(id).orElseThrow(() -> new NotFoundException(Radar.class, id));
  }
}
