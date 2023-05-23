package ru.hh.techradar.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hh.techradar.entity.Radar;
import ru.hh.techradar.exception.NotFoundException;
import ru.hh.techradar.filter.ComponentFilter;
import ru.hh.techradar.repository.RadarRepository;

@Service
public class RadarService {
  private final BlipEventService blipEventService;
  private final RadarRepository radarRepository;
  private final QuadrantService quadrantService;
  private final RingService ringService;
  private final BlipService blipService;
  private final CompanyService companyService;

  public RadarService(
      BlipEventService blipEventService,
      RadarRepository radarRepository,
      QuadrantService quadrantService,
      RingService ringService,
      BlipService blipService,
      CompanyService companyService) {
    this.blipEventService = blipEventService;
    this.radarRepository = radarRepository;
    this.quadrantService = quadrantService;
    this.ringService = ringService;
    this.blipService = blipService;
    this.companyService = companyService;
  }


  @Transactional(readOnly = true)
  public Radar findById(Long id) {
    return radarRepository.findById(id).orElseThrow(() -> new NotFoundException(Radar.class, id));
  }

  @Transactional(readOnly = true)
  public List<Radar> findAllByFilter(Long companyId, Instant actualDate) {
    companyService.findById(companyId);
    actualDate = Optional.ofNullable(actualDate).orElse(Instant.now());
    return radarRepository.findAllByFilter(companyId, actualDate);
  }

  @Transactional(readOnly = true)
  public Radar findByIdAndFilter(Long id, Instant actualDate, Long blipEventId) {
    Instant date = getActualDate(actualDate, blipEventId);
    Radar radar = radarRepository.findById(id).orElseThrow(() -> new NotFoundException(Radar.class, id));
    radar.setQuadrants(quadrantService.findAllByFilter(new ComponentFilter().radarId(id).actualDate(date)));
    radar.setRings(ringService.findAllByFilter(new ComponentFilter().radarId(id).actualDate(date)));
    radar.setBlips(blipService.findAllByFilter(new ComponentFilter().radarId(id).actualDate(date)));
    return radar;
  }

  private Instant getActualDate(Instant actualDate, Long blipEventId) {
    if (actualDate != null) {
      return actualDate;
    } else if (blipEventId != null) {
      return blipEventService.findById(blipEventId).getCreationTime();
    }
    return Instant.now();
  }
}
