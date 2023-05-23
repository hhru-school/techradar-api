package ru.hh.techradar.service;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hh.techradar.entity.Radar;
import ru.hh.techradar.entity.Ring;
import ru.hh.techradar.exception.EntityExistsException;
import ru.hh.techradar.exception.NotFoundException;
import ru.hh.techradar.filter.ComponentFilter;
import ru.hh.techradar.filter.DateIdFilter;
import ru.hh.techradar.repository.RadarRepository;
import ru.hh.techradar.repository.RingRepository;

@Service
public class RingService {
  private final RingRepository ringRepository;
  private final RadarRepository radarRepository;

  public RingService(RingRepository ringRepository, RadarRepository radarRepository) {
    this.ringRepository = ringRepository;
    this.radarRepository = radarRepository;
  }

  @Transactional(readOnly = true)
  public List<Ring> findAllByFilter(ComponentFilter filter) {
    return ringRepository.findAllByFilter(filter);
  }

  @Transactional(readOnly = true)
  public Ring findById(Long id) {
    return ringRepository.findById(id).orElseThrow(() -> new NotFoundException(Ring.class, id));
  }

  @Transactional
  public Boolean archiveByFilter(DateIdFilter filter) {
    if (!Boolean.TRUE.equals(isContainBlipsByFilter(filter))) {
      return Boolean.FALSE;
    }
    Ring found = ringRepository.findById(filter.getId()).orElseThrow(() -> new NotFoundException(Ring.class, filter.getId()));
    if (Objects.isNull(found.getRemovedAt())) {
      found.setRemovedAt(Instant.now());
      ringRepository.update(found);
    }
    return Boolean.TRUE;
  }

  @Transactional
  public Ring update(Long id, Ring entity, Optional<DateIdFilter> filter) {
    Ring found = ringRepository.findById(id).orElseThrow(() -> new NotFoundException(Ring.class, id));
    if (ringRepository.isAnotherSuchRingExistByFilter(entity, filter.orElse(new DateIdFilter(found.getRadar().getId(), Instant.now())))) {
      throw new EntityExistsException(Ring.class, found.getCurrentSetting().getName());
    }
    found.setLastChangeTime(Instant.now());
    Optional.ofNullable(entity.getCurrentSetting()).ifPresent(
        ringSetting -> {
          var currentSetting = found.getCurrentSetting();
          ringSetting.setRing(found);
          if (!Objects.equals(ringSetting.getName(), currentSetting.getName())
              || !Objects.equals(ringSetting.getPosition(), currentSetting.getPosition())) {
            found.getSettings().add(ringSetting);
          }
        }
    );
    return ringRepository.update(found);
  }

  @Transactional
  public Ring save(Long radarId, Ring entity, Optional<DateIdFilter> filter) {
    entity.setRadar(radarRepository
        .findById(radarId)
        .orElseThrow(() -> new NotFoundException(Radar.class, radarId)));
    if (ringRepository.isAnotherSuchRingExistByFilter(entity, filter.orElse(new DateIdFilter(radarId, Instant.now())))) {
      throw new EntityExistsException(Ring.class, entity.getCurrentSetting().getName());
    }
    return ringRepository.save(entity);
  }

  @Transactional(readOnly = true)
  public Boolean isContainBlipsByFilter(DateIdFilter filter) {
    return ringRepository.isContainBlipsByFilter(filter);
  }
}
