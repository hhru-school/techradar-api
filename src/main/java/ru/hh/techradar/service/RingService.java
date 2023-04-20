package ru.hh.techradar.service;

import jakarta.inject.Inject;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hh.techradar.entity.Ring;
import ru.hh.techradar.exception.NotFoundException;
import ru.hh.techradar.mapper.RingMapperUnited;
import ru.hh.techradar.repository.RingRepository;

@Service
public class RingService implements BaseService<Long, Ring> {
  private final RingRepository ringRepository;
  private final RingMapperUnited ringMapperUnited;

  @Inject
  public RingService(RingRepository ringRepository, RingMapperUnited ringMapperUnited) {
    this.ringRepository = ringRepository;
    this.ringMapperUnited = ringMapperUnited;
  }

  @Override
  @Transactional(readOnly = true)
  public Ring findById(Long id) {
    return ringRepository.findById(id).orElseThrow(() -> new NotFoundException(Ring.class, id));
  }

  @Override
  @Transactional
  public void deleteById(Long id) {
    ringRepository.deleteById(id);
  }

  @Override
  @Transactional
  public Ring update(Long id, Ring entity) {
    Ring found = ringRepository.findById(id).orElseThrow(() -> new NotFoundException(Ring.class, id));
    return ringRepository.update(ringMapperUnited.toUpdate(found, entity));
  }

  @Override
  @Transactional
  public Ring save(Ring entity) {
    return ringRepository.save(entity);
  }

  @Override
  @Transactional(readOnly = true)
  public List<Ring> findAll() {
    return ringRepository.findAll();
  }
}
