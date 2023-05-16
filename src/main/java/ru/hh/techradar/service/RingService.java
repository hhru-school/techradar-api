package ru.hh.techradar.service;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hh.techradar.entity.Radar;
import ru.hh.techradar.entity.Ring;
import ru.hh.techradar.entity.RingSetting;
import ru.hh.techradar.exception.EntityExistsException;
import ru.hh.techradar.exception.NotFoundException;
import ru.hh.techradar.filter.ComponentFilter;
import ru.hh.techradar.repository.RingRepository;

@Service
public class RingService {
  private final static Integer MAX_RING_POSITION = 8;
  private final static Integer MIN_RING_POSITION = 1;
  private final RingRepository ringRepository;

  public RingService(RingRepository ringRepository) {
    this.ringRepository = ringRepository;
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
  public Boolean archiveById(Long id) {
    if (isContainBlipsById(id) == Boolean.FALSE) {
      Ring found = ringRepository.findById(id).orElseThrow(() -> new NotFoundException(Ring.class, id));
      if (Objects.isNull(found.getRemovedAt())) {
        found.setRemovedAt(Instant.now());
        ringRepository.update(found);
        return Boolean.TRUE;
      }
    }
    return Boolean.FALSE;
  }

  @Transactional
  public Boolean removeById(Long id) {
    if (isContainBlipsById(id) == Boolean.FALSE) {
      Ring found = ringRepository.findById(id).orElseThrow(() -> new NotFoundException(Ring.class, id));
      ringRepository.delete(found);
      return Boolean.TRUE;
    }
    return Boolean.FALSE;
  }

  @Transactional
  public Ring update(Long id, Ring entity) {
    Ring found = ringRepository.findById(id).orElseThrow(() -> new NotFoundException(Ring.class, id));
    found.setLastChangeTime(Instant.now());
    Optional.ofNullable(entity.getCurrentSetting()).ifPresent(
        ringSetting -> {
          var currentSetting = found.getCurrentSetting();
          ringSetting.setName(Optional.ofNullable(ringSetting.getName()).orElseGet(currentSetting::getName));
          Integer newPosition = Optional.ofNullable(ringSetting.getPosition()).orElseGet(currentSetting::getPosition);
          ringSetting.setPosition(
              (newPosition < MIN_RING_POSITION || newPosition > MAX_RING_POSITION) ? currentSetting.getPosition() : newPosition
          );
          ringSetting.setRing(found);
          if (Objects.equals(ringSetting.getName(), currentSetting.getName())
              && Objects.equals(ringSetting.getPosition(), currentSetting.getPosition())) {
            throw new EntityExistsException(RingSetting.class, currentSetting.getId());
          }
          found.getSettings().add(ringSetting);
        }
    );
    return ringRepository.update(found);
  }

  @Transactional
  public Ring save(Radar radar, Ring entity) {
    Objects.requireNonNull(radar);
    Objects.requireNonNull(entity);
    entity.setRadar(radar);
    return ringRepository.save(entity);
  }

  @Transactional(readOnly = true)
  public Boolean isContainBlipsById(Long id) {
    return ringRepository.isContainBlipsById(id);
  }

  @Transactional
  public void forceRemoveById(Long id) {
    ringRepository.forceRemoveById(id);
  }
}
