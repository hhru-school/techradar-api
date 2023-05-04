package ru.hh.techradar.service;

import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hh.techradar.entity.Blip;
import ru.hh.techradar.exception.NotFoundException;
import ru.hh.techradar.filter.BlipComponentFilter;
import ru.hh.techradar.filter.ComponentFilter;
import ru.hh.techradar.mapper.BlipMapper;
import ru.hh.techradar.repository.BlipRepository;

@Service
public class BlipService implements BaseService<Long, Blip> {
  private final BlipMapper blipMapper;
  private final BlipRepository blipRepository;


  public BlipService(
      BlipMapper blipMapper,
      BlipRepository blipRepository
  ) {
    this.blipMapper = blipMapper;
    this.blipRepository = blipRepository;
  }

  @Override
  @Transactional(readOnly = true)
  public Blip findById(Long id) {
    return blipRepository.findById(id).orElseThrow(() -> new NotFoundException(Blip.class, id));
  }

  @Override
  @Transactional
  public void deleteById(Long id) {
    blipRepository.deleteById(id);
  }

  @Override
  @Transactional
  public Blip update(Long id, Blip entity) {
    Blip found = blipRepository.findById(id).orElseThrow(() -> new NotFoundException(Blip.class, id));
    found.setLastChangeTime(Instant.now());
    return blipRepository.update(blipMapper.toUpdate(found, entity));
  }

  @Override
  @Transactional
  public Blip save(Blip entity) {
    return blipRepository.save(entity);
  }

  @Override
  @Transactional(readOnly = true)
  public List<Blip> findAll() {
    return blipRepository.findAll();
  }

  @Transactional(readOnly = true)
  public Blip findByIdAndFilter(BlipComponentFilter filter) {
    return blipRepository.findByIdAndFilter(filter);
  }

  @Transactional(readOnly = true)
  public List<Blip> findAllByFilter(ComponentFilter filter) {
    return blipRepository.findActualBlipsByFilter(filter);
  }
}
