package ru.hh.techradar.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hh.techradar.dto.RadarDto;
import ru.hh.techradar.entity.Radar;
import ru.hh.techradar.exception.NotFoundException;
import ru.hh.techradar.mapper.BlipMapper;
import ru.hh.techradar.mapper.QuadrantMapper;
import ru.hh.techradar.mapper.RadarMapper;
import ru.hh.techradar.mapper.RingMapper;
import ru.hh.techradar.repository.RadarRepository;

@Service
public class RadarService {
  private final RadarRepository radarRepository;
  private final CompanyService companyService;
  private final UserService userService;
  private final RadarMapper radarMapper;
  private final QuadrantMapper quadrantMapper;
  private final RingMapper ringMapper;
  private final BlipMapper blipMapper;

  public RadarService(
      RadarRepository radarRepository,
      CompanyService companyService, UserService userService, RadarMapper radarMapper, QuadrantMapper quadrantMapper, RingMapper ringMapper,
      BlipMapper blipMapper) {
    this.radarRepository = radarRepository;
    this.companyService = companyService;
    this.userService = userService;
    this.radarMapper = radarMapper;
    this.quadrantMapper = quadrantMapper;
    this.ringMapper = ringMapper;
    this.blipMapper = blipMapper;
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

  @Transactional
  public Radar save(RadarDto dto) {
    Radar radar = radarMapper.toEntity(dto);
    radar.setAuthor(userService.findById(dto.getAuthorId()));
    radar.setCompany(companyService.findById(dto.getCompanyId()));
    Optional.ofNullable(dto.getQuadrants()).ifPresent(q -> radar.setQuadrants(quadrantMapper.toEntities(q)));
    Optional.ofNullable(dto.getRings()).ifPresent(r -> radar.setRings(ringMapper.toEntities(r)));
    Optional.ofNullable(dto.getBlips()).ifPresent(b -> radar.setBlips(blipMapper.toEntities(b)));
    return radarRepository.save(radar);
  }
}
