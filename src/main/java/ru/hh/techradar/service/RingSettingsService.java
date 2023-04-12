package ru.hh.techradar.service;

import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Service;
import ru.hh.techradar.entity.RingSetting;
import ru.hh.techradar.exception.NotFoundException;
import ru.hh.techradar.repository.RingSettingRepository;

@Service
public class RingSettingsService implements BaseService<Long, RingSetting> {

  private final RingSettingRepository ringSettingRepository;

  public RingSettingsService(RingSettingRepository ringSettingRepository) {
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

  @Override
  @Transactional
  public RingSetting update(Long id, RingSetting entity) {
    RingSetting found = ringSettingRepository.findById(id).orElseThrow(() -> new NotFoundException(RingSetting.class, id));
//    return radarRepository.update(companyMapper.toUpdate(found, entity));
    return null;
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
}
