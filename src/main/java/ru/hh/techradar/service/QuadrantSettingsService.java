package ru.hh.techradar.service;

import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Service;
import ru.hh.techradar.entity.QuadrantSetting;
import ru.hh.techradar.exception.NotFoundException;
import ru.hh.techradar.repository.QuadrantSettingRepository;

@Service
public class QuadrantSettingsService implements BaseService<Long, QuadrantSetting> {

  private final QuadrantSettingRepository quadrantSettingRepository;

  public QuadrantSettingsService(QuadrantSettingRepository quadrantSettingRepository) {
    this.quadrantSettingRepository = quadrantSettingRepository;
  }

  @Override
  @Transactional
  public QuadrantSetting findById(Long id) {
    return quadrantSettingRepository.findById(id).orElseThrow(() -> new NotFoundException(QuadrantSetting.class, id));
  }

  @Override
  @Transactional
  public void deleteById(Long id) {
    quadrantSettingRepository.deleteById(id);
  }

  @Override
  @Transactional
  public QuadrantSetting update(Long id, QuadrantSetting entity) {
    QuadrantSetting found = quadrantSettingRepository.findById(id).orElseThrow(() -> new NotFoundException(QuadrantSetting.class, id));
//    return radarRepository.update(companyMapper.toUpdate(found, entity));
    return null;
  }

  @Override
  @Transactional
  public QuadrantSetting save(QuadrantSetting entity) {
    return quadrantSettingRepository.save(entity);
  }

  @Override
  @Transactional
  public List<QuadrantSetting> findAll() {
    return quadrantSettingRepository.findAll();
  }
}
