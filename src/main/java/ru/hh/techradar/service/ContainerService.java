package ru.hh.techradar.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hh.techradar.dto.ContainerCreateDto;
import ru.hh.techradar.entity.Blip;
import ru.hh.techradar.entity.BlipEvent;
import ru.hh.techradar.entity.Container;
import ru.hh.techradar.entity.Quadrant;
import ru.hh.techradar.entity.Radar;
import ru.hh.techradar.entity.Ring;
import ru.hh.techradar.entity.User;
import ru.hh.techradar.filter.ComponentFilter;
import ru.hh.techradar.filter.RevisionFilter;
import ru.hh.techradar.mapper.BlipCreateMapper;
import ru.hh.techradar.mapper.QuadrantMapper;
import ru.hh.techradar.mapper.RingMapper;

@Service
public class ContainerService {
  private final BlipEventService blipEventService;
  private final QuadrantService quadrantService;
  private final RingService ringService;
  private final BlipService blipService;
  private final QuadrantMapper quadrantMapper;
  private final RingMapper ringMapper;
  private final RadarService radarService;
  private final BlipCreateMapper blipCreateMapper;
  private final RadarVersionService radarVersionService;
  private final UserService userService;

  public ContainerService(
      BlipEventService blipEventService,
      QuadrantService quadrantService,
      RingService ringService,
      BlipService blipService,
      QuadrantMapper quadrantMapper,
      RingMapper ringMapper,
      RadarService radarService,
      BlipCreateMapper blipCreateMapper, RadarVersionService radarVersionService, UserService userService) {
    this.blipEventService = blipEventService;
    this.quadrantService = quadrantService;
    this.ringService = ringService;
    this.blipService = blipService;
    this.quadrantMapper = quadrantMapper;
    this.ringMapper = ringMapper;
    this.radarService = radarService;
    this.blipCreateMapper = blipCreateMapper;
    this.radarVersionService = radarVersionService;
    this.userService = userService;
  }

  @Transactional(readOnly = true)
  public Container findByBlipEventId(Long blipEventId) {
    Container container = new Container();
    BlipEvent blipEvent = blipEventService.findById(blipEventId);
    Radar radar = blipEvent.getBlip().getRadar();
    container.setRadar(radar);
    container.setBlipEvent(blipEvent);
    container.setQuadrants(quadrantService.findAllByFilter(new ComponentFilter().radarId(radar.getId()).actualDate(blipEvent.getCreationTime())));
    container.setRings(ringService.findAllByFilter(new ComponentFilter().radarId(radar.getId()).actualDate(blipEvent.getCreationTime())));
    container.setBlips(blipService.findAllByBlipEventId(blipEvent.getId()));
    return container;
  }

  @Transactional(readOnly = true)
  public Container findByRadarVersionId(Long radarVersionId) {
    return findByBlipEventId(radarVersionService.findById(radarVersionId).getBlipEvent().getId());
  }

  @Transactional(readOnly = true)
  public Container findByFilter(RevisionFilter filter) {
    if (filter.getBlipEventId() == null) {
      return findByRadarVersionId(filter.getRadarVersionId());
    }
    return findByBlipEventId(filter.getBlipEventId());
  }

  @Transactional
  public Container save(ContainerCreateDto dto, String username) {
    Container container = new Container();
    User user = userService.findByUsername(username);
    Radar radar = radarService.save(dto.getRadar());

    container.setRadar(radar);

    //TODO: refactor this two methods when saveAll will be available in quadrant and ring services
    Map<String, Quadrant> nameToQuadrant = saveQuadrants(dto, container);
    Map<String, Ring> nameToRing = saveRings(dto, container);

    List<Blip> blips = blipService.saveAll(blipCreateMapper.toEntities(dto.getBlips()), radar);
    container.setBlips(blips);

    List<BlipEvent> blipEvents = prepareBlipEvents(dto, user, nameToQuadrant, nameToRing, blips);
    blipEvents = blipEventService.fillParentsAndSave(blipEvents, null);
    container.setBlipEvent(blipEvents.isEmpty() ? null : blipEvents.get(blipEvents.size() - 1));
    return container;
  }

  private List<BlipEvent> prepareBlipEvents(
      ContainerCreateDto dto,
      User user,
      Map<String, Quadrant> nameToQuadrant,
      Map<String, Ring> nameToRing,
      List<Blip> blips) {
    List<BlipEvent> blipEvents = new ArrayList<>();
    for (int i = 0; i < blips.size(); i++) {
      blipEvents.add(getBlipEvent(dto, user, nameToQuadrant, nameToRing, blips, i));
    }
    return blipEvents;
  }

  private BlipEvent getBlipEvent(
      ContainerCreateDto dto,
      User user,
      Map<String, Quadrant> nameToQuadrant,
      Map<String, Ring> nameToRing,
      List<Blip> blips,
      int i) {
    BlipEvent blipEvent = new BlipEvent();
    blipEvent.setComment("init");
    blipEvent.setBlip(blips.get(i));
    String quadrantName = dto.getBlips().get(i).getQuadrant().getName();
    blipEvent.setQuadrant(nameToQuadrant.get(quadrantName));
    String ringName = dto.getBlips().get(i).getRing().getName();
    blipEvent.setRing(nameToRing.get(ringName));
    blipEvent.setUser(user);
    return blipEvent;
  }

  private Map<String, Ring> saveRings(ContainerCreateDto dto, Container container) {
    Map<String, Ring> nameToRing = new HashMap<>();
    List<Ring> rings = ringMapper.toEntities(dto.getRings());
    rings.forEach(r -> {
      String ringName = r.getCurrentSetting().getName();
      Ring ring = ringService.save(container.getRadar().getId(), r, Optional.empty());
      nameToRing.put(ringName, ring);
    });
    container.setRings(rings);
    return nameToRing;
  }

  private Map<String, Quadrant> saveQuadrants(ContainerCreateDto dto, Container container) {
    Map<String, Quadrant> nameToQuadrant = new HashMap<>();
    List<Quadrant> quadrants = quadrantMapper.toEntities(dto.getQuadrants());
    quadrants.forEach(q ->
    {
      String quadrantName = q.getCurrentSetting().getName();
      Quadrant quadrant = quadrantService.save(container.getRadar().getId(), q, Optional.empty());
      nameToQuadrant.put(quadrantName, quadrant);
    });
    container.setQuadrants(quadrants);
    return nameToQuadrant;
  }
}
