package ru.hh.techradar.service;

import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Service;
import ru.hh.techradar.entity.Blip;
import ru.hh.techradar.exception.NotFoundException;
import ru.hh.techradar.repository.BlipRepository;

@Service
public class BlipService implements BaseService<Long, Blip> {

  private final BlipRepository blipRepository;

  public BlipService(BlipRepository blipRepository) {
    this.blipRepository = blipRepository;
  }

  @Override
  @Transactional
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
//    return blipRepository.update(companyMapper.toUpdate(found, entity));
    return null;
  }

  @Override
  @Transactional
  public Blip save(Blip entity) {
    return blipRepository.save(entity);
  }

  @Override
  @Transactional
  public List<Blip> findAll() {
    return blipRepository.findAll();
  }
}
