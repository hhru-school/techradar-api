package ru.hh.techradar.service;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hh.techradar.entity.Quadrant;
import ru.hh.techradar.entity.Radar;
import ru.hh.techradar.entity.Ring;
import ru.hh.techradar.exception.EntityExistsException;
import ru.hh.techradar.exception.NotFoundException;
import ru.hh.techradar.exception.UniqueException;
import ru.hh.techradar.filter.ComponentFilter;
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
  public Quadrant update(Long id, Quadrant entity) {
    Quadrant found = quadrantRepository.findById(id).orElseThrow(() -> new NotFoundException(Quadrant.class, id));
    if (found.getName().equals(entity.getName())) {
      return found;
    }
    validateUnique(found.getRadar().getId(), entity);
    found.setName(entity.getName());
    return quadrantRepository.update(found);
  }

  @Transactional
  public List<Quadrant> save(Long radarId, Collection<Quadrant> quadrants) {
    validateUnique(quadrants);
    Radar radar = radarRepository.findById(radarId).orElseThrow(() -> new NotFoundException(Radar.class, radarId));
    return quadrants.stream().map(quadrant -> save(radar, quadrant)).toList();
  }

  private void validateUnique(Collection<Quadrant> quadrants) {
    Set<String> names = quadrants.stream()
        .map(Quadrant::getName)
        .filter(name -> Collections.frequency(quadrants.stream().map(Quadrant::getName).toList(), name) > 1)
        .collect(Collectors.toSet());
    if (!names.isEmpty()) {
      throw new UniqueException(String.format("Quadrant collection contains not unique names: %s", names));
    }
    Set<Integer> positions = quadrants.stream()
        .map(Quadrant::getPosition)
        .filter(position -> Collections.frequency(quadrants.stream().map(Quadrant::getPosition).toList(), position) > 1)
        .collect(Collectors.toSet());
    if (!positions.isEmpty()) {
      throw new UniqueException(String.format("Quadrant collection contains not unique positions: %s", positions));
    }
  }

  private Quadrant save(Radar radar, Quadrant entity) {
    entity.setRadar(radar);
    return quadrantRepository.save(entity);
  }

  private void validateUnique(Long radarId, Quadrant entity) {
    Optional<Quadrant> quadrant = quadrantRepository.findByNameAndRadarId(entity.getName(), radarId);
    if (quadrant.isPresent()) {
      throw new EntityExistsException(Quadrant.class, entity.getName());
    }
  }
}
