package ru.hh.techradar.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hh.techradar.dto.ContainerCreateDto;
import ru.hh.techradar.entity.Blip;
import ru.hh.techradar.entity.BlipEvent;
import ru.hh.techradar.entity.Container;
import ru.hh.techradar.entity.Quadrant;
import ru.hh.techradar.entity.Radar;
import ru.hh.techradar.entity.Ring;
import ru.hh.techradar.filter.ComponentFilter;
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

  public ContainerService(
      BlipEventService blipEventService,
      QuadrantService quadrantService,
      RingService ringService,
      BlipService blipService,
      QuadrantMapper quadrantMapper,
      RingMapper ringMapper,
      RadarService radarService,
      BlipCreateMapper blipCreateMapper) {
    this.blipEventService = blipEventService;
    this.quadrantService = quadrantService;
    this.ringService = ringService;
    this.blipService = blipService;
    this.quadrantMapper = quadrantMapper;
    this.ringMapper = ringMapper;
    this.radarService = radarService;
    this.blipCreateMapper = blipCreateMapper;
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

  @Transactional
  public Container save(ContainerCreateDto dto) {
    Container container = new Container();
    Radar radar = radarService.save(dto.getRadar());
    container.setRadar(radar);

    List<Quadrant> quadrants = quadrantMapper.toEntities(dto.getQuadrants());
    Map<String, Quadrant> nameToQuadrant = new HashMap<>();
    quadrants.forEach(q ->
    {
      String quadrantName = q.getCurrentSetting().getName();
      Quadrant quadrant = quadrantService.save(radar.getId(), q);
      nameToQuadrant.put(quadrantName, quadrant);
    });
    container.setQuadrants(quadrants);

    List<Ring> rings = ringMapper.toEntities(dto.getRings());
    Map<String, Ring> nameToRing = new HashMap<>();
    rings.forEach(r -> {
      String ringName = r.getCurrentSetting().getName();
      Ring ring = ringService.save(radar, r);
      nameToRing.put(ringName, ring);
    });
    container.setRings(rings);

    List<Blip> blips = blipCreateMapper.toEntities(dto.getBlips());
    List<BlipEvent> blipEvents = new ArrayList<>();
    for (int i = 0; i < blips.size(); i++) {
      Blip blip = blips.get(i);
      blip.setRadar(radar);
      Blip savedBlip = blipService.save(blip);
      BlipEvent blipEvent = new BlipEvent();
      blipEvent.setComment("init");
      blipEvent.setBlip(savedBlip);
      String quadrantName = dto.getBlips().get(i).getQuadrant().getName();
      blipEvent.setQuadrant(nameToQuadrant.get(quadrantName));
      String ringName = dto.getBlips().get(i).getRing().getName();
      blipEvent.setRing(nameToRing.get(ringName));
      blipEvent.setUser(radar.getAuthor());//TODO: insert correct user
      blipEvents.add(blipEvent);
    }
    if (blipEvents.size() > 0) {
      BlipEvent firstBlipEvent = blipEvents.get(0);
      firstBlipEvent.setParentId(null);
      BlipEvent lastSavedBlipEvent = blipEventService.save(firstBlipEvent);
      if (blipEvents.size() > 1) {
        for (int i = 1; i < blipEvents.size(); i++) {
          blipEvents.get(i).setParentId(lastSavedBlipEvent.getId());
          lastSavedBlipEvent = blipEventService.save(blipEvents.get(i));
        }
      }
    }
    container.setBlips(blips);
    container.setBlipEvent(blipEvents.get(blipEvents.size() - 1));
    return container;
  }
}
