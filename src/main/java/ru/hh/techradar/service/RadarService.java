package ru.hh.techradar.service;

import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Service;
import ru.hh.techradar.entity.Radar;
import ru.hh.techradar.exception.NotFoundException;
import ru.hh.techradar.repository.RadarRepository;

@Service
public class RadarService implements BaseService<Long, Radar> {

  private final RadarRepository radarRepository;

  public RadarService(RadarRepository radarRepository) {
    this.radarRepository = radarRepository;
  }

  @Override
  @Transactional
  public Radar findById(Long id) {
    return radarRepository.findById(id).orElseThrow(() -> new NotFoundException(Radar.class, id));
  }

  @Override
  @Transactional
  public void deleteById(Long id) {
    radarRepository.deleteById(id);
  }

  @Override
  @Transactional
  public Radar update(Long id, Radar entity) {
    Radar found = radarRepository.findById(id).orElseThrow(() -> new NotFoundException(Radar.class, id));
//    return radarRepository.update(companyMapper.toUpdate(found, entity));
    return null;
  }

  @Override
  @Transactional
  public Radar save(Radar entity) {
    return radarRepository.save(entity);
  }

  @Override
  @Transactional
  public List<Radar> findAll() {
    return radarRepository.findAll();
  }
}
