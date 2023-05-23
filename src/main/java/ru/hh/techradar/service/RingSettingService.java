package ru.hh.techradar.service;

import jakarta.inject.Inject;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
  @Transactional(readOnly = true)
  public RingSetting findById(Long id) {
    return ringSettingRepository.findById(id).orElseThrow(() -> new NotFoundException(RingSetting.class, id));
  }

  @Override
  @Transactional
  public void deleteById(Long id) {
    ringSettingRepository.deleteById(id);
  }


  @Override
  @Transactional
  public RingSetting update(Long id, RingSetting entity) {
    return ringSettingRepository.update(entity);
  }

  @Override
  @Transactional
  public RingSetting save(RingSetting entity) {
    return ringSettingRepository.save(entity);
  }

  @Override
  @Transactional(readOnly = true)
  public List<RingSetting> findAll() {
    return ringSettingRepository.findAll();
  }

  @Transactional(readOnly = true)
  public RingSetting findRingSettingByDate(Long ringId, Instant date) {
    return ringSettingRepository.findRingSettingByDate(ringId, date).orElseThrow(() -> new NotFoundException(RingSetting.class, ringId));
  }
}
