package ru.hh.techradar.service;

import jakarta.validation.Valid;
import jakarta.validation.Validator;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hh.techradar.entity.Ring;
import ru.hh.techradar.exception.EntityExistsException;
import ru.hh.techradar.exception.NotFoundException;
import ru.hh.techradar.exception.UniqueException;
import ru.hh.techradar.filter.ComponentFilter;
import ru.hh.techradar.mapper.RingMapper;
import ru.hh.techradar.repository.RingRepository;

@Service
public class RingService {
  public static final String RING_COLLECTION_CONTAINS_NOT_UNIQUE_NAMES = "Ring collection contains not unique names: %s";
  public static final String RING_COLLECTION_CONTAINS_NOT_UNIQUE_POSITIONS = "Ring collection contains not unique positions: %s";
  private final RingRepository ringRepository;
  private final RingMapper ringMapper;
  private final Validator validator;

  public RingService(RingRepository ringRepository, RingMapper ringMapper, Validator validator) {
    this.ringRepository = ringRepository;
    this.ringMapper = ringMapper;
    this.validator = validator;
  }

  @Transactional(readOnly = true)
  public List<Ring> findAllByFilter(@Valid ComponentFilter filter) {
    return ringRepository.findAllByFilter(filter);
  }

  @Transactional(readOnly = true)
  public Ring findById(Long id) {
    return ringRepository.findById(id).orElseThrow(() -> new NotFoundException(Ring.class, id));
  }

  @Transactional
  public Ring update(Long id, Ring ring) {
    Ring found = ringRepository.findById(id).orElseThrow(() -> new NotFoundException(Ring.class, id));
    if (found.getName().equals(ring.getName())) {
      return found;
    }
    ring = ringMapper.update(found, ring);
    validateUnique(ring);
    validator.validate(ring);
    return ringRepository.update(ring);
  }

  private Ring save(@Valid Ring ring) {
    return ringRepository.save(ring);
  }

  @Transactional
  public List<Ring> save(@Valid Collection<Ring> rings) {
    validateUnique(rings);
    return rings.stream().map(this::save).toList();
  }

  private void validateUnique(@Valid Ring ring) {
    Optional<Ring> found = ringRepository.findByNameAndRadarId(ring.getName(), ring.getRadar().getId());
    if (found.isPresent()) {
      throw new EntityExistsException(Ring.class, ring.getName());
    }
  }

  // TODO reformat duplicated checks
  private void validateUnique(@Valid Collection<Ring> rings) {
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
}
