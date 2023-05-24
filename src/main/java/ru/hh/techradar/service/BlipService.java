package ru.hh.techradar.service;

import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hh.techradar.entity.Blip;
import ru.hh.techradar.exception.NotFoundException;
import ru.hh.techradar.filter.BlipFilter;
import ru.hh.techradar.filter.ComponentFilter;
import ru.hh.techradar.mapper.BlipMapper;
import ru.hh.techradar.repository.BlipRepository;

@Service
public class BlipService {
  private final BlipMapper blipMapper;
  private final BlipRepository blipRepository;

  public BlipService(
      BlipMapper blipMapper,
      BlipRepository blipRepository) {
    this.blipMapper = blipMapper;
    this.blipRepository = blipRepository;
  }

  @Transactional
  public Blip save(Blip blip) {
    return blipRepository.save(blip);
  }

  @Transactional(readOnly = true)
  public List<Blip> findAll() {
    return blipRepository.findAll();
  }

  @Transactional(readOnly = true)
  public Blip findByFilter(BlipFilter filter) {
    return blipRepository.findByFilter(filter);
  }

  @Transactional(readOnly = true)
  public List<Blip> findAllByBlipEventId(Long blipEventId) {
    return blipRepository.findActualBlipsByBlipEventId(blipEventId);
  }

  @Transactional(readOnly = true)
  public List<Blip> findAllByFilter(ComponentFilter filter) {
    return blipRepository.findActualBlipsByFilter(filter);
  }

  @Transactional(readOnly = true)
  public Blip findById(Long id) {
    return blipRepository.findById(id).orElseThrow(() -> new NotFoundException(Blip.class, id));
  }


  @Transactional
  public Blip update(Long id, Blip entity) {
    Blip found = blipRepository.findById(id).orElseThrow(() -> new NotFoundException(Blip.class, id));
    found.setLastChangeTime(Instant.now());
    return blipRepository.update(blipMapper.toUpdate(found, entity));
  }

  @Transactional
  public void deleteById(Long id) {
    blipRepository.deleteById(id);
  }
}
