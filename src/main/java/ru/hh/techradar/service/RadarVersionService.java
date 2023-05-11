package ru.hh.techradar.service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hh.techradar.dto.RadarVersionDto;
import ru.hh.techradar.entity.RadarVersion;
import ru.hh.techradar.exception.NotFoundException;
import ru.hh.techradar.mapper.RadarVersionMapper;
import ru.hh.techradar.repository.RadarVersionRepository;

@Service
public class RadarVersionService {
  private final RadarVersionRepository radarVersionRepository;
  private final RadarVersionMapper radarVersionMapper;
  private final BlipEventService blipEventService;
//  private final RadarService radarService;

  public RadarVersionService(RadarVersionRepository radarVersionRepository, RadarVersionMapper radarVersionMapper,
      BlipEventService blipEventService
//      RadarService radarService
  ) {
    this.radarVersionRepository = radarVersionRepository;
    this.radarVersionMapper = radarVersionMapper;
    this.blipEventService = blipEventService;
//    this.radarService = radarService;
  }

  @Transactional(readOnly = true)
  public RadarVersion findById(Long id) {
    return radarVersionRepository.findById(id).orElseThrow(() -> new NotFoundException(RadarVersion.class, id));
  }
  @Transactional
  public void deleteById(Long id) {
    radarVersionRepository.deleteById(id);
  }
  @Transactional
  public RadarVersion update(Long id, RadarVersionDto dto) {
    RadarVersion target = findById(id);
    RadarVersion source = radarVersionMapper.toEntity(dto);
    Optional.ofNullable(dto.getName()).ifPresent(target::setName);
    Optional.ofNullable(dto.getRelease()).ifPresent(target::setRelease);
    Optional.ofNullable(dto.getBlipEventId())
        .ifPresent(beId -> target.setBlipEvent(blipEventService.findById(beId)));
    return target;
  }
  @Transactional
  public RadarVersion save(RadarVersion radarVersion) {
    return radarVersionRepository.save(radarVersion);
  }
  @Transactional(readOnly = true)
  public List<RadarVersion> findAll() {
    return radarVersionRepository.findAll();
  }
  @Transactional(readOnly = true)
  public Collection<RadarVersion> findAllByRadarId(Long radarId) {
    return radarVersionRepository.findAllByRadarId(radarId);
  }
}
