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
import ru.hh.techradar.filter.DateIdFilter;
import ru.hh.techradar.repository.QuadrantRepository;
import ru.hh.techradar.repository.RadarRepository;

@Service
public class QuadrantService {
  private final QuadrantRepository quadrantRepository;
  private final RadarRepository radarRepository;

  public QuadrantService(
      QuadrantRepository quadrantRepository,
      RadarRepository radarRepository) {
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
  public Quadrant update(Long id, Quadrant entity, Optional<DateIdFilter> filter) {
    Quadrant found = quadrantRepository.findById(id).orElseThrow(() -> new NotFoundException(Quadrant.class, id));
    if (quadrantRepository.isAnotherSuchQuadrantExistByFilter(entity, filter.orElse(new DateIdFilter(found.getRadar().getId(), Instant.now())))) {
      throw new EntityExistsException(Quadrant.class, found.getCurrentSetting().getName());
    }
    found.setLastChangeTime(Instant.now());
    Optional.ofNullable(entity.getCurrentSetting()).ifPresent(
        quadrantSetting -> {
          var currentSetting = Optional.ofNullable(found.getCurrentSetting()).orElseThrow(() -> new NotFoundException(QuadrantSetting.class, 1));
          quadrantSetting.setQuadrant(found);
          if (!Objects.equals(quadrantSetting.getPosition(), currentSetting.getPosition())
              || !Objects.equals(quadrantSetting.getName(), currentSetting.getName())) {
            found.getSettings().add(quadrantSetting);
          }
        }
    );
    return quadrantRepository.update(found);
  }

  @Transactional
  public Quadrant save(Long radarId, Quadrant entity, Optional<DateIdFilter> filter) {
    entity.setRadar(radarRepository
        .findById(radarId)
        .orElseThrow(() -> new NotFoundException(Radar.class, radarId)));
    if (quadrantRepository.isAnotherSuchQuadrantExistByFilter(entity, filter.orElse(new DateIdFilter(radarId, Instant.now())))) {
      throw new EntityExistsException(Quadrant.class, entity.getCurrentSetting().getName());
    }
    return quadrantRepository.save(entity);
  }

  @Transactional(readOnly = true)
  public Boolean isContainBlipsByFilter(DateIdFilter filter) {
    return quadrantRepository.isContainBlipsByFilter(filter);
  }

  @Transactional
  public Boolean archiveByFilter(DateIdFilter filter) {
    if (!Boolean.TRUE.equals(isContainBlipsByFilter(filter))) {
      return Boolean.FALSE;
    }
    Quadrant found = quadrantRepository.findById(filter.getId()).orElseThrow(() -> new NotFoundException(Quadrant.class, filter.getId()));
    if (Objects.isNull(found.getRemovedAt())) {
      found.setRemovedAt(Instant.now());
      quadrantRepository.update(found);
    }
    return Boolean.TRUE;
  }
}
