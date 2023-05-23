package ru.hh.techradar.service;

import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hh.techradar.entity.BlipEvent;
import ru.hh.techradar.exception.NotFoundException;
import ru.hh.techradar.filter.BlipFilter;
import ru.hh.techradar.mapper.BlipEventMapper;
import ru.hh.techradar.repository.BlipEventRepository;

@Service
public class BlipEventService implements BaseService<Long, BlipEvent> {
  private final BlipEventMapper blipEventMapper;
  private final BlipEventRepository blipEventRepository;

  public BlipEventService(BlipEventMapper blipEventMapper, BlipEventRepository blipEventRepository) {
    this.blipEventMapper = blipEventMapper;
    this.blipEventRepository = blipEventRepository;
  }

  @Override
  @Transactional(readOnly = true)
  public BlipEvent findById(Long id) {
    return blipEventRepository.findById(id).orElseThrow(() -> new NotFoundException(BlipEvent.class, id));
  }

  @Override
  @Transactional
  public void deleteById(Long id) {
    blipEventRepository.deleteById(id);
  }

  @Override
  @Transactional
  public BlipEvent update(Long id, BlipEvent entity) {
    BlipEvent found = blipEventRepository.findById(id).orElseThrow(() -> new NotFoundException(BlipEvent.class, id));
    found.setLastChangeTime(Instant.now());
    return blipEventRepository.update(blipEventMapper.toUpdate(found, entity));
  }

  @Override
  @Transactional
  public BlipEvent save(BlipEvent entity) {
    return blipEventRepository.save(entity);
  }

  @Override
  @Transactional(readOnly = true)
  public List<BlipEvent> findAll() {
    return blipEventRepository.findAll();
  }

  @Transactional(readOnly = true)
  public BlipEvent findActualBlipEventByBlipIdAndInstant(BlipFilter filter) {
    return blipEventRepository.findActualBlipEventByFilter(filter).orElseThrow(IllegalArgumentException::new);
  }
}
