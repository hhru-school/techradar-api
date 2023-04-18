package ru.hh.techradar.service;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Service;
import ru.hh.techradar.entity.Ring;
import ru.hh.techradar.exception.NotFoundException;
import ru.hh.techradar.mapper.RingMapper;
import ru.hh.techradar.repository.RingRepository;

@Service
public class RingService implements BaseService<Long, Ring> {

  private final RingRepository ringRepository;
  private final RingMapper ringMapper;

  @Inject
  public RingService(RingRepository ringRepository, RingMapper ringMapper) {
    this.ringRepository = ringRepository;
    this.ringMapper = ringMapper;
  }

  @Override
  @Transactional
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
    return ringRepository.update(ringMapper.toUpdate(found, entity));
  }

  @Override
  @Transactional
  public Ring save(Ring entity) {
    return ringRepository.save(entity);
  }

  @Override
  @Transactional
  public List<Ring> findAll() {
    return ringRepository.findAll();
  }

  @Transactional
  public List<Ring> fetchRingsByRadarId(Long radarId, Instant actualDate) {
    return ringRepository.fetchRingsByRadarId(radarId, actualDate);
  }
}
