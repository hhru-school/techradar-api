package ru.hh.techradar.service;

import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Service;
import ru.hh.techradar.entity.Quadrant;
import ru.hh.techradar.exception.NotFoundException;
import ru.hh.techradar.repository.QuadrantRepository;

@Service
public class QuadrantService implements BaseService<Long, Quadrant> {

  private final QuadrantRepository quadrantRepository;

  public QuadrantService(QuadrantRepository quadrantRepository) {
    this.quadrantRepository = quadrantRepository;
  }

  @Override
  @Transactional
  public Quadrant findById(Long id) {
    return quadrantRepository.findById(id).orElseThrow(() -> new NotFoundException(Quadrant.class, id));
  }

  @Override
  @Transactional
  public void deleteById(Long id) {
    quadrantRepository.deleteById(id);
  }

  @Override
  @Transactional
  public Quadrant update(Long id, Quadrant entity) {
    Quadrant found = quadrantRepository.findById(id).orElseThrow(() -> new NotFoundException(Quadrant.class, id));
//    return radarRepository.update(companyMapper.toUpdate(found, entity));
    return null;
  }

  @Override
  @Transactional
  public Quadrant save(Quadrant entity) {
    return quadrantRepository.save(entity);
  }

  @Override
  @Transactional
  public List<Quadrant> findAll() {
    return quadrantRepository.findAll();
  }
}
