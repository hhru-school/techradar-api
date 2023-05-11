package ru.hh.techradar.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hh.techradar.dto.RadarDto;
import ru.hh.techradar.entity.Blip;
import ru.hh.techradar.entity.BlipEvent;
import ru.hh.techradar.entity.Quadrant;
import ru.hh.techradar.entity.Radar;
import ru.hh.techradar.entity.RadarVersion;
import ru.hh.techradar.entity.Ring;
import ru.hh.techradar.exception.NotFoundException;
import ru.hh.techradar.filter.ComponentFilter;
import ru.hh.techradar.mapper.BlipMapper;
import ru.hh.techradar.mapper.QuadrantMapper;
import ru.hh.techradar.mapper.RadarMapper;
import ru.hh.techradar.mapper.RingMapper;
import ru.hh.techradar.repository.RadarRepository;

@Service
public class RadarService {
  private final BlipEventService blipEventService;
  private final RadarRepository radarRepository;
  private final QuadrantService quadrantService;
  private final RingService ringService;
  private final BlipService blipService;
  private final CompanyService companyService;
  private final RadarVersionService radarVersionService;
  private final RadarMapper radarMapper;
  private final UserService userService;
  private final QuadrantMapper quadrantMapper;
  private final RingMapper ringMapper;
  private final BlipMapper blipMapper;

  public RadarService(
      BlipEventService blipEventService,
      RadarRepository radarRepository,
      QuadrantService quadrantService,
      RingService ringService,
      BlipService blipService,
      CompanyService companyService, RadarVersionService radarVersionService, RadarMapper radarMapper, UserService userService,
      QuadrantMapper quadrantMapper, RingMapper ringMapper, BlipMapper blipMapper) {
    this.blipEventService = blipEventService;
    this.radarRepository = radarRepository;
    this.quadrantService = quadrantService;
    this.ringService = ringService;
    this.blipService = blipService;
    this.companyService = companyService;
    this.radarVersionService = radarVersionService;
    this.radarMapper = radarMapper;
    this.userService = userService;
    this.quadrantMapper = quadrantMapper;
    this.ringMapper = ringMapper;
    this.blipMapper = blipMapper;
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

  //TODO: refactor this
  @Transactional(readOnly = true)
  public Radar findByIdAndFilter(Long id, Instant actualDate, Long radarVersionId) {
    RadarVersion radarVersion = radarVersionService.findById(radarVersionId);
    BlipEvent blipEvent = radarVersion.getBlipEvent();
    Long blipEventId = blipEvent.getId();
    Instant date = getActualDate(actualDate, blipEventId);
    Radar radar = radarRepository.findById(id).orElseThrow(() -> new NotFoundException(Radar.class, id));
//    radar.setQuadrants(quadrantService.findAllByFilter(new ComponentFilter().radarId(id).actualDate(date)));
//    radar.setRings(ringService.findAllByFilter(new ComponentFilter().radarId(id).actualDate(date)));
//    radar.setBlips(blipService.findAllByBlipEventId(blipEventId));
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

  //  @Transactional//TODO: refactor this
//  public Radar save(RadarDto dto) {
//    Radar radar = radarMapper.toEntity(dto);
//    Optional.ofNullable(dto.getAuthorId()).ifPresent(a -> radar.setAuthor(userService.findById(a)));
//    Optional.ofNullable(dto.getCompanyId()).ifPresent(c -> radar.setCompany(companyService.findById(c)));
//    Radar savedRadar = radarRepository.save(radar);
//    Map<String, Quadrant> nameToQuadrant = new HashMap<>();
//    Map<String, Ring> nameToRing = new HashMap<>();
//    Optional.ofNullable(dto.getQuadrants()).ifPresent(q -> radar.setQuadrants(quadrantMapper.toEntities(q)));
//    if (!Objects.isNull(radar.getQuadrants())) {
//      radar.getQuadrants().forEach(q -> q.setRadar(savedRadar));
//    }
//    Optional.ofNullable(dto.getRings()).ifPresent(r -> radar.setRings(ringMapper.toEntities(r)));
//    if (!Objects.isNull(radar.getRings())) {
//      radar.getRings().forEach(q -> q.setRadar(savedRadar));
//    }
//    Optional.ofNullable(dto.getBlips()).ifPresent(b -> radar.setBlips(blipMapper.toEntities(b)));
//    if (!Objects.isNull(radar.getBlips())) {
//      List<Quadrant> quadrants = quadrantService.findAllByFilter(new ComponentFilter().radarId(savedRadar.getId()));
//      quadrants.forEach(q -> nameToQuadrant.put());//HERE!!
//      List<BlipEvent> blipEvents = new ArrayList<>();
//      radar.getBlips().forEach(b -> {
//        b.setRadar(savedRadar);
//        Blip savedBlip = blipService.save(b);
//        BlipEvent blipEvent = new BlipEvent();
//        blipEvent.setBlip(savedBlip);
//        blipEvent.setQuadrant();
////        blipEvent.setBlip();
//      });
//    }
//    return savedRadar;
//  }
  @Transactional
  public Radar save(RadarDto dto) {
    Radar radar = radarMapper.toEntity(dto);
    radar.setAuthor(userService.findById(dto.getAuthorId()));
    radar.setCompany(companyService.findById(dto.getCompanyId()));
//    Optional.ofNullable(dto.getAuthorId()).ifPresent(a -> radar.setAuthor(userService.findById(a)));
//    Optional.ofNullable(dto.getCompanyId()).ifPresent(c -> radar.setCompany(companyService.findById(c)));
    return radarRepository.save(radar);
  }
}
