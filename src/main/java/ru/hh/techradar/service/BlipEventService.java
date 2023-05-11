package ru.hh.techradar.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hh.techradar.dto.BlipEventDto;
import ru.hh.techradar.entity.BlipEvent;
import ru.hh.techradar.exception.NotFoundException;
import ru.hh.techradar.filter.BlipFilter;
import ru.hh.techradar.mapper.BlipEventMapper;
import ru.hh.techradar.repository.BlipEventRepository;

@Service
public class BlipEventService {
  private final BlipEventMapper blipEventMapper;
  private final BlipEventRepository blipEventRepository;
  private final BlipService blipService;
  private final QuadrantService quadrantService;
  private final RingService ringService;
  private final UserService userService;

  public BlipEventService(BlipEventMapper blipEventMapper, BlipEventRepository blipEventRepository,
      BlipService blipService,
      QuadrantService quadrantService,
      RingService ringService, UserService userService) {
    this.blipEventMapper = blipEventMapper;
    this.blipEventRepository = blipEventRepository;
    this.blipService = blipService;
    this.quadrantService = quadrantService;
    this.ringService = ringService;
    this.userService = userService;
  }

  @Transactional(readOnly = true)
  public BlipEvent findById(Long id) {
    return blipEventRepository.findById(id).orElseThrow(() -> new NotFoundException(BlipEvent.class, id));
  }

  @Transactional
  public void deleteById(Long id) {
    blipEventRepository.deleteById(id);
  }

  @Transactional
  public BlipEvent update(BlipEventDto dto) {
    Long id = dto.getId();
    BlipEvent entity = fillBlipEvent(dto);
    BlipEvent found = blipEventRepository.findById(id).orElseThrow(() -> new NotFoundException(BlipEvent.class, id));
    found.setLastChangeTime(Instant.now());
    return blipEventRepository.update(blipEventMapper.toUpdate(found, entity));
  }

  @Transactional
  public BlipEvent save(BlipEventDto dto) {
    BlipEvent blipEvent = fillBlipEvent(dto);
    return blipEventRepository.save(blipEvent);
  }

  private BlipEvent fillBlipEvent(BlipEventDto dto) {
    BlipEvent blipEvent = blipEventMapper.toEntity(dto);
    Optional.ofNullable(dto.getParentId()).ifPresent(blipEvent::setParentId);
    blipEvent.setParentId(dto.getParentId());
    blipEvent.setBlip(blipService.findById(dto.getBlipId()));
    blipEvent.setQuadrant(quadrantService.findById(dto.getQuadrantId()));
    blipEvent.setRing(ringService.findById(dto.getRingId()));
    blipEvent.setUser(userService.findById(dto.getAuthorId()));
    return blipEvent;
  }

  @Transactional
  public BlipEvent save(BlipEvent blipEvent) {
    return blipEventRepository.save(blipEvent);
  }
    @Transactional(readOnly = true)
  public List<BlipEvent> findAll() {
    return blipEventRepository.findAll();
  }

  @Transactional(readOnly = true)
  public BlipEvent findActualBlipEventByBlipIdAndInstant(BlipFilter filter) {
    return blipEventRepository.findActualBlipEventByFilter(filter).orElseThrow(IllegalArgumentException::new);
  }

  @Transactional//TODO: not ready (must be checked for correctness)
  public BlipEvent insertWithNext(BlipEventDto newBlipEventDto, BlipEventDto nextBlipEventDto) {
    BlipEvent savedBlipEvent = save(newBlipEventDto);
    nextBlipEventDto.setParentId(savedBlipEvent.getId());
    return savedBlipEvent;
  }
  @Transactional//TODO: think of returning value
  public BlipEvent insert(BlipEventDto blipEventDto) {
    BlipEvent savedBlipEvent = save(blipEventDto);
    blipEventRepository.updateBrothersToBeChildren(savedBlipEvent);
    return savedBlipEvent;
  }

  @Transactional
  public BlipEvent moveAndInsert(BlipEventDto blipEventDto) {
    BlipEvent foundBlipEvent = findById(blipEventDto.getId());
    blipEventRepository.updateChildrenToBeBrothers(foundBlipEvent);
    BlipEvent updatedBlipEvent = update(blipEventDto);
    blipEventRepository.updateBrothersToBeChildren(updatedBlipEvent);
    return updatedBlipEvent;
  }
  @Transactional(readOnly = true)
  public List<BlipEvent> findAllBlipsByBlipEventId(Long blipEventId) {
    return blipEventRepository.findAllBlipEventsByBlipEventId(blipEventId);
  }
}
