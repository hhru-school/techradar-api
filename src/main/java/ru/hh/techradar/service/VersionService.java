package ru.hh.techradar.service;

import java.util.Collection;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hh.techradar.dto.RadarDto;
import ru.hh.techradar.entity.Radar;
import ru.hh.techradar.mapper.RadarMapper;
import ru.hh.techradar.repository.VersionRepository;

@Service
public class VersionService {
  private final VersionRepository versionRepository;
  private final RadarMapper radarMapper;

  public VersionService(VersionRepository versionRepository, RadarMapper radarMapper) {
    this.versionRepository = versionRepository;
    this.radarMapper = radarMapper;
  }
  @Transactional(readOnly = true)
  public Collection<Radar> findAllByFilter(Long radarId) {
    return versionRepository.findAllByFilter(radarId);
  }
  @Transactional(readOnly = true)
  public RadarDto findRevisionOfRadarById(Long radarId, Long revisionId) {
    Radar radar = versionRepository.findRevisionOfRadarById(radarId, revisionId);
    return radarMapper.toDto(radar);
  }
}
