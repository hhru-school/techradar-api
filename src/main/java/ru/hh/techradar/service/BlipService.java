package ru.hh.techradar.service;

import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hh.techradar.entity.Blip;
import ru.hh.techradar.exception.NotFoundException;
import ru.hh.techradar.mapper.BlipMapper;
import ru.hh.techradar.repository.BlipRepository;

@Service
public class BlipService {
  private final BlipMapper blipMapper;
  private final BlipRepository blipRepository;
  private final QuadrantService quadrantService;
  private final RingService ringService;
  private final RadarService radarService;


  public BlipService(
      BlipMapper blipMapper,
      BlipRepository blipRepository,
      QuadrantService quadrantService, RingService ringService, RadarService radarService) {
    this.blipMapper = blipMapper;
    this.blipRepository = blipRepository;
    this.quadrantService = quadrantService;
    this.ringService = ringService;
    this.radarService = radarService;
  }

  @Transactional(readOnly = true)
  public Blip findById(Long id) {
    return blipRepository.findById(id).orElseThrow(() -> new NotFoundException(Blip.class, id));
  }

  @Transactional
  public void deleteById(Long id) {
    blipRepository.deleteById(id);
  }

  @Transactional
  public Blip update(Long id, Blip entity) {
    Blip found = blipRepository.findById(id).orElseThrow(() -> new NotFoundException(Blip.class, id));
    found.setLastChangeTime(Instant.now());
    return blipRepository.update(blipMapper.toUpdate(found, entity));
  }

  @Transactional
  public Blip save(Blip entity, Long quadrantId, Long ringId, Long radarId) {
    entity.setQuadrant(quadrantService.findById(quadrantId));
    entity.setRing(ringService.findById(ringId));
    entity.setRadar(radarService.findById(radarId));
    return blipRepository.save(entity);
  }

  @Transactional(readOnly = true)
  public List<Blip> findAll() {
    return blipRepository.findAll();
  }

  @Transactional(readOnly = true)
  public Blip findByIdAndActualDate(Long blipId, Instant actualDate) {
    return blipRepository.findByIdAndActualDate(blipId, actualDate);
  }

//  @Transactional(readOnly = true)
//  public Blip findByIdAndBlipEventId(Long blipId, Long blipEventId) {
//    BlipEvent blipEvent = blipEventRepository.findById(blipEventId).orElseThrow(() -> new NotFoundException(BlipEvent.class, blipEventId));
//    return findByIdAndActualDate(blipId, blipEvent.getCreationTime());
//  }
//
//  @Transactional(readOnly = true)
//  public List<Blip> findAllByFilter(Long radarId, Long blipEventTestId) {
//    return blipRepository.findActualBlipsByRadarIdAndActualDate(radarId, blipEventTestId);
//  }
}
