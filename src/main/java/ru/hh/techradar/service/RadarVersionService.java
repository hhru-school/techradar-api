package ru.hh.techradar.service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hh.techradar.dto.RadarVersionDto;
import ru.hh.techradar.entity.RadarVersion;
import ru.hh.techradar.exception.NotFoundException;
import ru.hh.techradar.repository.RadarVersionRepository;

@Service
public class RadarVersionService {
  private final RadarVersionRepository radarVersionRepository;
  private final BlipEventService blipEventService;

  public RadarVersionService(RadarVersionRepository radarVersionRepository,
      BlipEventService blipEventService
  ) {
    this.radarVersionRepository = radarVersionRepository;
    this.blipEventService = blipEventService;
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
