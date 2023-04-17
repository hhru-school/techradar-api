package ru.hh.techradar.service;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import ru.hh.techradar.entity.Ring;
import ru.hh.techradar.entity.RingSetting;
import ru.hh.techradar.exception.NotFoundException;
import ru.hh.techradar.repository.RingSettingRepository;

@Service
public class RingSettingService implements BaseService<Long, RingSetting> {
  private final RingSettingRepository ringSettingRepository;

  @Inject
  public RingSettingService(RingSettingRepository ringSettingRepository) {
    this.ringSettingRepository = ringSettingRepository;
  }

  @Override
  @Transactional
  public RingSetting findById(Long id) {
    return ringSettingRepository.findById(id).orElseThrow(() -> new NotFoundException(RingSetting.class, id));
  }

  @Override
  @Transactional
  public void deleteById(Long id) {
    ringSettingRepository.deleteById(id);
  }

  @Deprecated
  @Override
  @Transactional
  public RingSetting update(Long id, RingSetting entity) {
    throw new UnsupportedOperationException("Use Ring method instead!");
  }

  @Override
  @Transactional
  public RingSetting save(RingSetting entity) {
    return ringSettingRepository.save(entity);
  }

  @Override
  @Transactional
  public List<RingSetting> findAll() {
    return ringSettingRepository.findAll();
  }

  @Transactional
  public Optional<RingSetting> findRingSettingByDate(Ring ring, Instant date) {
    return ringSettingRepository.findRingSettingByDate(ring, date);
  }
}
