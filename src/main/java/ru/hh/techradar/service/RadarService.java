package ru.hh.techradar.service;

import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hh.techradar.entity.BlipEvent;
import ru.hh.techradar.entity.Radar;
import ru.hh.techradar.exception.NotFoundException;
import ru.hh.techradar.repository.RadarRepository;

@Service
public class RadarService implements BaseService<Long, Radar> {
  private final BlipEventService blipEventService;
  private final RadarRepository radarRepository;
  private final QuadrantService quadrantService;
  private final RingService ringService;
  private final BlipService blipService;

  public RadarService(
      BlipEventService blipEventService,
      RadarRepository radarRepository,
      QuadrantService quadrantService,
      RingService ringService,
      BlipService blipService
  ) {
    this.blipEventService = blipEventService;
    this.radarRepository = radarRepository;
    this.quadrantService = quadrantService;
    this.ringService = ringService;
    this.blipService = blipService;
  }


  @Override
  public Radar findById(Long id) {
    return radarRepository.findById(id).orElseThrow(() -> new NotFoundException(Radar.class, id));
  }

  @Override
  public void deleteById(Long id) {

  }

  @Override
  public Radar update(Long id, Radar entity) {
    return null;
  }

  @Override
  public Radar save(Radar entity) {
    return null;
  }

  @Override
  public List<Radar> findAll() {
    return null;
  }
@Transactional
  public Radar findByIdAndBlipEventId(Long id, Long blipEventId) {
    BlipEvent blipEvent = blipEventService.findById(blipEventId);
    Instant actualDate = blipEvent.getCreationTime();
    Radar radar = radarRepository.findById(id).orElseThrow(() -> new NotFoundException(Radar.class, id));
    radar.setQuadrants(quadrantService.findAllByFilter(radar.getId(), actualDate));
    radar.setRings(ringService.findAllByFilter(radar.getId(), actualDate));
    radar.setBlips(blipService.findAllByFilter(radar.getId(), actualDate));
    return radar;
  }
}
