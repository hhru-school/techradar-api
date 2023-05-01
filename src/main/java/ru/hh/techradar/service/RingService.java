package ru.hh.techradar.service;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hh.techradar.entity.Radar;
import ru.hh.techradar.entity.Ring;
import ru.hh.techradar.exception.NotFoundException;
import ru.hh.techradar.repository.RingRepository;

@Service
public class RingService {
  private final RingRepository ringRepository;

  public RingService(RingRepository ringRepository) {
    this.ringRepository = ringRepository;
  }

  @Transactional(readOnly = true)
  public List<Ring> findAllByFilter(Long radarId, Instant actualDate) {
    if (Objects.isNull(actualDate)) {
      actualDate = Instant.now();
    }
    return ringRepository.findAllByFilter(radarId, actualDate);
  }

  @Transactional(readOnly = true)
  public Ring findById(Long id) {
    return ringRepository.findById(id).orElseThrow(() -> new NotFoundException(Ring.class, id));

  }

//  @Transactional
//  public void archiveById(Long id) {
//    Ring found = ringRepository.findById(id).orElseThrow(() -> new NotFoundException(Ring.class, id));
//    if (Objects.isNull(found.getRemovedAt())) {
//      found.setRemovedAt(Instant.now());
//      ringRepository.update(found);
//    }
//  }
//
//  @Transactional
//  public Ring update(Long id, Ring entity) {
//    Ring found = ringRepository.findById(id).orElseThrow(() -> new NotFoundException(Ring.class, id));
//    found.setLastChangeTime(Instant.now());
//    RingSetting setting = entity.getCurrentSetting();
//    setting.setRing(found);
//    found.getSettings().add(setting);
//    return ringRepository.update(found);
//  }

  @Transactional
  public Ring save(Radar radar, Ring entity) {
    entity.setRadar(radar);
    return ringRepository.save(entity);
  }
}
