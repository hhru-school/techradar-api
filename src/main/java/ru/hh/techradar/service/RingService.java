package ru.hh.techradar.service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
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
  public static final String RING_COLLECTION_CONTAINS_NOT_UNIQUE_NAMES = "Ring collection contains not unique names: %s";
  public static final String RING_COLLECTION_CONTAINS_NOT_UNIQUE_POSITIONS = "Ring collection contains not unique positions: %s";
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
    validateUnique(found.getRadar().getId(), entity);
    found.setName(entity.getName());
    return ringRepository.update(found);
  }

  @Transactional
  public List<Ring> save(Long radarId, Collection<Ring> rings) {
    validateUnique(rings);
    Radar radar = radarRepository.findById(radarId).orElseThrow(() -> new NotFoundException(Radar.class, radarId));
    return rings.stream().map(ring -> save(radar, ring)).toList();
  }

  private void validateUnique(Collection<Ring> rings) {
    Set<String> names = rings.stream()
        .map(Ring::getName)
        .filter(name -> Collections.frequency(rings.stream().map(Ring::getName).toList(), name) > 1)
        .collect(Collectors.toSet());
    if (!names.isEmpty()) {
      throw new UniqueException(String.format(RING_COLLECTION_CONTAINS_NOT_UNIQUE_NAMES, names));
    }
    Set<Integer> positions = rings.stream()
        .map(Ring::getPosition)
        .filter(position -> Collections.frequency(rings.stream().map(Ring::getPosition).toList(), position) > 1)
        .collect(Collectors.toSet());
    if (!positions.isEmpty()) {
      throw new UniqueException(String.format(RING_COLLECTION_CONTAINS_NOT_UNIQUE_POSITIONS, positions));
    }
  }

  private Ring save(Radar radar, Ring entity) {
    entity.setRadar(radar);
    return ringRepository.save(entity);
  }

  private void validateUnique(Long radarId, Ring entity) {
    Optional<Ring> ring = ringRepository.findByNameAndRadarId(entity.getName(), radarId);
    if (ring.isPresent()) {
      throw new EntityExistsException(Ring.class, entity.getName());
    }
  }
}
