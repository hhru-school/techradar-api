package ru.hh.techradar.service;

import java.util.Collection;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hh.techradar.entity.Radar;
import ru.hh.techradar.entity.Ring;
import ru.hh.techradar.exception.EntityExistsException;
import ru.hh.techradar.exception.NotFoundException;
import ru.hh.techradar.exception.UniqueException;
import ru.hh.techradar.filter.ComponentFilter;
import ru.hh.techradar.repository.RadarRepository;
import ru.hh.techradar.repository.RingRepository;

@Service
public class RingService {
  private final RingRepository ringRepository;
  private final RadarRepository radarRepository;

  public RingService(
      RingRepository ringRepository,
      RadarRepository radarRepository) {
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
  public Ring update(Long id, Ring entity) {
    Ring found = ringRepository.findById(id).orElseThrow(() -> new NotFoundException(Ring.class, id));
    if (found.getName().equals(entity.getName())) {
      return found;
    }
    isRingValid(found.getRadar().getId(), entity);
    found.setName(entity.getName());
    return ringRepository.update(found);
  }

  @Transactional
  public Ring save(Long radarId, Ring entity) {
    Radar radar = radarRepository.findById(radarId).orElseThrow(() -> new NotFoundException(Radar.class, radarId));
    return save(radar, entity);
  }

  @Transactional
  public List<Ring> save(Long radarId, Collection<Ring> rings) {
    isRingsValid(rings);
    Radar radar = radarRepository.findById(radarId).orElseThrow(() -> new NotFoundException(Radar.class, radarId));
    return rings.stream().map(ring -> save(radar, ring)).toList();
  }

  private void isRingsValid(Collection<Ring> rings) {
    if (rings.size() != rings.stream().map(Ring::getName).distinct().count()) {
      throw new UniqueException("Ring collection contains not unique names!");
    }
    if (rings.size() != rings.stream().map(Ring::getPosition).distinct().count()) {
      throw new UniqueException("Ring collection contains not unique positions!");
    }
  }

  private Ring save(Radar radar, Ring entity) {
    entity.setRadar(radar);
    return ringRepository.save(entity);
  }

  private void isRingValid(Long radarId, Ring entity) {
    List<Ring> rings = ringRepository.findAllByFilter(new ComponentFilter().radarId(radarId));
    if (rings.stream().anyMatch(ring -> ring.getName().equals(entity.getName()))) {
      throw new EntityExistsException(Ring.class, entity.getName());
    }
  }
}
