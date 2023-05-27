package ru.hh.techradar.service;

import java.util.Collection;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hh.techradar.entity.Quadrant;
import ru.hh.techradar.entity.Radar;
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
    isQuadrantValid(found.getRadar().getId(), entity);
    found.setName(entity.getName());
    return quadrantRepository.update(found);
  }

  @Transactional
  public Quadrant save(Long radarId, Quadrant entity) {
    Radar radar = radarRepository.findById(radarId).orElseThrow(() -> new NotFoundException(Radar.class, radarId));
    return save(radar, entity);
  }

  @Transactional
  public List<Quadrant> save(Long radarId, Collection<Quadrant> quadrants) {
    isQuadrantsValid(quadrants);
    Radar radar = radarRepository.findById(radarId).orElseThrow(() -> new NotFoundException(Radar.class, radarId));
    return quadrants.stream().map(quadrant -> save(radar, quadrant)).toList();
  }

  private void isQuadrantsValid(Collection<Quadrant> quadrants) {
    if (quadrants.size() != quadrants.stream().map(Quadrant::getName).distinct().count()) {
      throw new UniqueException("Quadrant collection contains not unique names!");
    }
    if (quadrants.size() != quadrants.stream().map(Quadrant::getPosition).distinct().count()) {
      throw new UniqueException("Quadrant collection contains not unique positions!");
    }
  }

  private Quadrant save(Radar radar, Quadrant entity) {
    entity.setRadar(radar);
    return quadrantRepository.save(entity);
  }

  private void isQuadrantValid(Long radarId, Quadrant entity) {
    List<Quadrant> quadrants = quadrantRepository.findAllByFilter(new ComponentFilter().radarId(radarId));
    if (quadrants.stream().anyMatch(quadrant -> quadrant.getName().equals(entity.getName()))) {
      throw new EntityExistsException(Quadrant.class, entity.getName());
    }
  }
}
