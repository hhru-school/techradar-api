package ru.hh.techradar.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hh.techradar.entity.BlipTemplate;
import ru.hh.techradar.exception.NotFoundException;
import ru.hh.techradar.mapper.BlipTemplateMapper;
import ru.hh.techradar.repository.BlipTemplateRepository;

@Service
public class BlipTemplateService implements BaseService<String, BlipTemplate> {
  private final BlipTemplateRepository blipTemplateRepository;
  private final BlipTemplateMapper blipTemplateMapper;

  public BlipTemplateService(BlipTemplateRepository blipTemplateRepository, BlipTemplateMapper blipTemplateMapper) {
    this.blipTemplateRepository = blipTemplateRepository;
    this.blipTemplateMapper = blipTemplateMapper;
  }

  @Transactional(readOnly = true)
  @Override
  public BlipTemplate findById(String id) {
    return blipTemplateRepository.findById(id).orElseThrow(() -> new NotFoundException(BlipTemplate.class, id));
  }

  @Transactional
  @Override
  public void deleteById(String id) {
    blipTemplateRepository.deleteById(id);
  }

  @Transactional
  @Override
  public BlipTemplate update(String id, BlipTemplate entity) {
    return blipTemplateRepository.update(blipTemplateMapper.update(entity, findById(id)));
  }

  @Transactional
  @Override
  public BlipTemplate save(BlipTemplate entity) {
    return blipTemplateRepository.save(entity);
  }

  @Transactional(readOnly = true)
  @Override
  public List<BlipTemplate> findAll() {
    return blipTemplateRepository.findAll();
  }

  @Transactional(readOnly = true)
  public List<BlipTemplate> findByNamePart(String namePart) {
    return blipTemplateRepository.findByNamePart(namePart);
  }
}
