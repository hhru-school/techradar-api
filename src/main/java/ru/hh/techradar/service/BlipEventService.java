package ru.hh.techradar.service;

import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Service;
import ru.hh.techradar.entity.BlipEvent;
import ru.hh.techradar.exception.NotFoundException;
import ru.hh.techradar.repository.BlipEventRepository;

@Service
public class BlipEventService implements BaseService<Long, BlipEvent> {

  private final BlipEventRepository blipEventRepository;

  public BlipEventService(BlipEventRepository blipEventRepository) {
    this.blipEventRepository = blipEventRepository;
  }


  @Override
  @Transactional
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
//    return blipLogRepository.update(companyMapper.toUpdate(found, entity));
    return null;
  }

  @Override
  @Transactional
  public BlipEvent save(BlipEvent entity) {
    return blipEventRepository.save(entity);
  }

  @Override
  @Transactional
  public List<BlipEvent> findAll() {
    return blipEventRepository.findAll();
  }
}
