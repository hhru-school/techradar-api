package ru.hh.techradar.service;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hh.techradar.entity.Quadrant;
import ru.hh.techradar.entity.QuadrantSetting;
import ru.hh.techradar.exception.NotFoundException;
import ru.hh.techradar.repository.QuadrantRepository;

@Service
public class QuadrantService {
  private final QuadrantRepository quadrantRepository;
  private final RadarService radarService;

  public QuadrantService(
      QuadrantRepository quadrantRepository,
      RadarService radarService) {
    this.quadrantRepository = quadrantRepository;
    this.radarService = radarService;
  }

  @Transactional(readOnly = true)
  public List<Quadrant> findAllByFilter(Long radarId, Instant actualDate) {
    if (Objects.isNull(actualDate)) {
      actualDate = Instant.now();
    }
    return quadrantRepository.findAllByFilter(radarId, actualDate);
  }

  @Transactional(readOnly = true)
  public Quadrant findById(Long id) {
    return quadrantRepository.findById(id).orElseThrow(() -> new NotFoundException(Quadrant.class, id));

  }

  @Transactional
  public void archiveById(Long id) {
    Quadrant found = quadrantRepository.findById(id).orElseThrow(() -> new NotFoundException(Quadrant.class, id));
    if (Objects.isNull(found.getRemovedAt())) {
      found.setRemovedAt(Instant.now());
      quadrantRepository.update(found);
    }
  }

  @Transactional
  public Quadrant update(Long id, Quadrant entity) {
    Quadrant found = quadrantRepository.findById(id).orElseThrow(() -> new NotFoundException(Quadrant.class, id));
    found.setLastChangeTime(Instant.now());
    QuadrantSetting setting = entity.getCurrentSetting();
    setting.setQuadrant(found);
    found.getSettings().add(setting);
    return quadrantRepository.update(found);
  }

  @Transactional
  public Quadrant save(Long radarId, Quadrant entity) {
    entity.setRadar(radarService.findById(radarId));
    return quadrantRepository.save(entity);
  }
}
