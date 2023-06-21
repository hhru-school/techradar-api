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
import org.springframework.validation.annotation.Validated;
import ru.hh.techradar.entity.Quadrant;
import ru.hh.techradar.entity.Radar;
import ru.hh.techradar.exception.EntityExistsException;
import ru.hh.techradar.exception.NotFoundException;
import ru.hh.techradar.exception.UniqueException;
import ru.hh.techradar.filter.ComponentFilter;
import ru.hh.techradar.mapper.QuadrantMapper;
import ru.hh.techradar.repository.QuadrantRepository;
import ru.hh.techradar.repository.RadarRepository;

@Service
@Validated
public class QuadrantService {
  public static final String QUADRANT_COLLECTION_CONTAINS_NOT_UNIQUE_NAMES = "Quadrant collection contains not unique names: %s";
  public static final String QUADRANT_COLLECTION_CONTAINS_NOT_UNIQUE_POSITIONS = "Quadrant collection contains not unique positions: %s";
  private final QuadrantRepository quadrantRepository;
  private final RadarRepository radarRepository;
  private final QuadrantMapper quadrantMapper;
  private final Validator validator;

  public QuadrantService(
      QuadrantRepository quadrantRepository,
      RadarRepository radarRepository, QuadrantMapper quadrantMapper, Validator validator) {
    this.quadrantRepository = quadrantRepository;
    this.radarRepository = radarRepository;
    this.quadrantMapper = quadrantMapper;
    this.validator = validator;
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
  public Quadrant update(Long id, Quadrant quadrant) {
    Quadrant found = quadrantRepository.findById(id).orElseThrow(() -> new NotFoundException(Quadrant.class, id));
    if (found.getName().equals(quadrant.getName())) {
      return found;
    }
    quadrant = quadrantMapper.update(found, quadrant);
    validateUnique(quadrant);
    validator.validate(quadrant);
    return quadrantRepository.update(quadrant);
  }

  private Quadrant save(Radar radar, Quadrant entity) {
    entity.setRadar(radar);
    return quadrantRepository.save(entity);
  }

  @Transactional
  public List<Quadrant> save(Long radarId, @Valid Collection<Quadrant> quadrants) {
    validateUnique(quadrants);
    Radar radar = radarRepository.findById(radarId).orElseThrow(() -> new NotFoundException(Radar.class, radarId));
    return quadrants.stream().map(quadrant -> save(radar, quadrant)).toList();
  }

  private void validateUnique(@Valid Quadrant entity) {
    Optional<Quadrant> quadrant = quadrantRepository.findByNameAndRadarId(entity.getName(), entity.getRadar().getId());
    if (quadrant.isPresent()) {
      throw new EntityExistsException(Quadrant.class, entity.getName());
    }
  }

  private void validateUnique(@Valid Collection<Quadrant> quadrants) {
    Set<String> names = quadrants.stream()
        .map(Quadrant::getName)
        .filter(name -> Collections.frequency(quadrants.stream().map(Quadrant::getName).toList(), name) > 1)
        .collect(Collectors.toSet());
    if (!names.isEmpty()) {
      throw new UniqueException(String.format(QUADRANT_COLLECTION_CONTAINS_NOT_UNIQUE_NAMES, names));
    }
    Set<Integer> positions = quadrants.stream()
        .map(Quadrant::getPosition)
        .filter(position -> Collections.frequency(quadrants.stream().map(Quadrant::getPosition).toList(), position) > 1)
        .collect(Collectors.toSet());
    if (!positions.isEmpty()) {
      throw new UniqueException(String.format(QUADRANT_COLLECTION_CONTAINS_NOT_UNIQUE_POSITIONS, positions));
    }
  }
}
