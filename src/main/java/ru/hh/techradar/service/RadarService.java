package ru.hh.techradar.service;

import org.springframework.stereotype.Service;
import ru.hh.techradar.entity.Radar;
import ru.hh.techradar.exception.NotFoundException;
import ru.hh.techradar.repository.RadarRepository;

@Service
public class RadarService {

  private final RadarRepository radarRepository;

  public RadarService(RadarRepository radarRepository) {
    this.radarRepository = radarRepository;
  }

  public Radar findById(Long id) {
    return radarRepository.findById(id).orElseThrow(() -> new NotFoundException(Radar.class, id));
  }
}
