package ru.hh.techradar.service;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hh.techradar.entity.Quadrant;
import ru.hh.techradar.entity.QuadrantSetting;
import ru.hh.techradar.entity.Radar;
import ru.hh.techradar.exception.EntityExistsException;
import ru.hh.techradar.exception.NotFoundException;
import ru.hh.techradar.filter.ComponentFilter;
import ru.hh.techradar.repository.QuadrantRepository;
import ru.hh.techradar.repository.RadarRepository;

@Service
public class QuadrantService {
  private final static Integer MAX_QUADRANT_POSITION = 8;
  private final static Integer MIN_QUADRANT_POSITION = 1;
  private final QuadrantRepository quadrantRepository;
  private final RadarRepository radarRepository;

  public QuadrantService(
      QuadrantRepository quadrantRepository,
      RadarRepository radarRepository
  ) {
    this.quadrantRepository = quadrantRepository;
    this.radarRepository = radarRepository;
  }

  @Transactional(readOnly = true)
  public List<Quadrant> findAllByFilter(ComponentFilter filter) {
    return quadrantRepository.findAllByFilter(filter);
  }

  @Transactional(readOnly = true)
  public Quadrant findById(Long id) {
    return quadrantRepository.findById(id).orElseThrow(() -> new NotFoundException(Quadrant.class, id));

  }

  @Transactional
  public Quadrant archiveById(Long id) {
    Quadrant found = quadrantRepository.findById(id).orElseThrow(() -> new NotFoundException(Quadrant.class, id));
    if (Objects.isNull(found.getRemovedAt())) {
      found.setRemovedAt(Instant.now());
      return quadrantRepository.update(found);
    }
    return found;
  }

  @Transactional
  public Quadrant update(Long id, Quadrant entity) {
    Quadrant found = quadrantRepository.findById(id).orElseThrow(() -> new NotFoundException(Quadrant.class, id));
    found.setLastChangeTime(Instant.now());
    Optional.ofNullable(entity.getCurrentSetting()).ifPresent(
        quadrantSetting -> {
          var currentSetting = found.getCurrentSetting();
          quadrantSetting.setName(Optional.ofNullable(quadrantSetting.getName()).orElseGet(currentSetting::getName));
          Integer newPosition = Optional.ofNullable(quadrantSetting.getPosition()).orElseGet(currentSetting::getPosition);
          quadrantSetting.setPosition(
              (newPosition < MIN_QUADRANT_POSITION || newPosition > MAX_QUADRANT_POSITION) ? currentSetting.getPosition() : newPosition
          );
          quadrantSetting.setQuadrant(found);
          if (Objects.equals(quadrantSetting.getName(), currentSetting.getName())
              && Objects.equals(quadrantSetting.getPosition(), currentSetting.getPosition())) {
            throw new EntityExistsException(QuadrantSetting.class, currentSetting.getId());
          }
          found.getSettings().add(quadrantSetting);
        }
    );
    return quadrantRepository.update(found);
  }

  @Transactional
  public Quadrant save(Long radarId, Quadrant entity) {
    entity.setRadar(radarRepository
        .findById(radarId)
        .orElseThrow(() -> new NotFoundException(Radar.class, radarId)));
    return quadrantRepository.save(entity);
  }
}
