package ru.hh.techradar.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hh.techradar.dto.ContainerCreateDto;
import ru.hh.techradar.entity.Blip;
import ru.hh.techradar.entity.BlipEvent;
import ru.hh.techradar.entity.Container;
import ru.hh.techradar.entity.Quadrant;
import ru.hh.techradar.entity.Radar;
import ru.hh.techradar.entity.RadarVersion;
import ru.hh.techradar.entity.Ring;
import ru.hh.techradar.entity.User;
import ru.hh.techradar.filter.ComponentFilter;
import ru.hh.techradar.filter.RevisionFilter;
import ru.hh.techradar.mapper.BlipCreateMapper;
import ru.hh.techradar.mapper.QuadrantMapper;
import ru.hh.techradar.mapper.RingMapper;

@Service
public class ContainerService {
  public static final String COMMENT_FOR_INITIAL_BLIP_EVENT = "_init_";
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
    BlipEvent blipEvent = blipEventService.findById(blipEventId);
    Container container = getContainerWithoutBlips(blipEventId);
    container.setBlips(blipService.findAllByBlipEventId(blipEvent.getId()));
    return container;
  }

  @Transactional(readOnly = true)
  public Container findByRadarVersionId(Long radarVersionId) {
    RadarVersion radarVersion = radarVersionService.findById(radarVersionId);
    Long blipEventId = radarVersion.getBlipEvent().getId();
    Container container = getContainerWithoutBlips(blipEventId);
    container.setBlips(blipService.findActualBlipsByRadarVersionWithDrawInfo(radarVersion));
    return container;
  }

  private Container getContainerWithoutBlips(Long blipEventId) {
    Container container = new Container();
    BlipEvent blipEvent = blipEventService.findById(blipEventId);
    Radar radar = blipEvent.getRadar();
    container.setRadar(radar);
    container.setBlipEvent(blipEvent);
    container.setQuadrants(quadrantService.findAllByFilter(new ComponentFilter().radarId(radar.getId()).actualDate(blipEvent.getCreationTime())));
    container.setRings(ringService.findAllByFilter(new ComponentFilter().radarId(radar.getId()).actualDate(blipEvent.getCreationTime())));
    return container;
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

    List<Ring> rings = ringService.save(container.getRadar().getId(),ringMapper.toEntities(dto.getRings()));
    container.setRings(rings);
    Map<String, Ring> nameToRing = getNameToRing(rings);

    List<Quadrant> quadrants = quadrantService.save(container.getRadar().getId(), quadrantMapper.toEntities(dto.getQuadrants()));
    container.setQuadrants(quadrants);
    Map<String, Quadrant> nameToQuadrant = getNameToQuadrant(quadrants);

    List<Blip> blips = blipService.saveAll(blipCreateMapper.toEntities(dto.getBlips()), radar);
    container.setBlips(blips);

    List<BlipEvent> blipEvents = prepareBlipEvents(dto, user, nameToQuadrant, nameToRing, blips, radar);
    blipEvents = blipEventService.fillParentsAndSave(blipEvents);
    radarVersionService.saveRootRadarVersion(radar, blipEvents.get(0));
    BlipEvent lastBlipEvent = blipEvents.get(blipEvents.size() - 1);
    container.setBlipEvent(lastBlipEvent);
    return container;
  }

  private static Map<String, Quadrant> getNameToQuadrant(List<Quadrant> quadrants) {
    return quadrants.stream().collect(Collectors.toMap(Quadrant::getName, Function.identity()));
  }

  private static Map<String, Ring> getNameToRing(List<Ring> rings) {
    return rings.stream().collect(Collectors.toMap(Ring::getName, Function.identity()));
  }

  private List<BlipEvent> prepareBlipEvents(
      ContainerCreateDto dto,
      User user,
      Map<String, Quadrant> nameToQuadrant,
      Map<String, Ring> nameToRing,
      List<Blip> blips,
      Radar radar) {
    List<BlipEvent> blipEvents = new ArrayList<>();
    BlipEvent rootBlipEvent = blipEventService.prepareRootBlipEvent(user, radar);
    blipEvents.add(rootBlipEvent);
    for (int i = 0; i < blips.size(); i++) {
      blipEvents.add(getBlipEvent(dto, user, nameToQuadrant, nameToRing, blips, radar, i));
    }
    return blipEvents;
  }

  private BlipEvent getBlipEvent(
      ContainerCreateDto dto,
      User user,
      Map<String, Quadrant> nameToQuadrant,
      Map<String, Ring> nameToRing,
      List<Blip> blips,
      Radar radar,
      int i) {
    BlipEvent blipEvent = new BlipEvent();
    blipEvent.setComment(COMMENT_FOR_INITIAL_BLIP_EVENT);
    blipEvent.setBlip(blips.get(i));
    String quadrantName = dto.getBlips().get(i).getQuadrant().getName();
    blipEvent.setQuadrant(nameToQuadrant.get(quadrantName));
    String ringName = dto.getBlips().get(i).getRing().getName();
    blipEvent.setRing(nameToRing.get(ringName));
    blipEvent.setUser(user);
    blipEvent.setRadar(radar);
    return blipEvent;
  }
}
