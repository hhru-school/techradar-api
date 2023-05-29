package ru.hh.techradar.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hh.techradar.dto.BlipEventDto;
import ru.hh.techradar.entity.BlipEvent;
import ru.hh.techradar.exception.NotFoundException;
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

  public BlipEventService(
      BlipEventMapper blipEventMapper, BlipEventRepository blipEventRepository,
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

  @Transactional
  public BlipEvent save(BlipEvent blipEvent) {
    return blipEventRepository.save(blipEvent);
  }

  @Transactional
  public BlipEvent save(BlipEventDto dto, Boolean isInsert) {
    if (isInsert != null && isInsert) {
      return insert(dto);
    }
    return isolatedSave(dto);
  }

  @Transactional
  public List<BlipEvent> fillParentsAndSave(List<BlipEvent> blipEvents, Long lastBlipEventId) {
    List<BlipEvent> result = new ArrayList<>();
    if (!blipEvents.isEmpty()) {
      for (int i = 0; i < blipEvents.size(); i++) {
        blipEvents.get(i).setParentId(lastBlipEventId);
        result.add(save(blipEvents.get(i)));
        lastBlipEventId = result.get(i).getId();
      }
    }
    return result;
  }

  @Transactional//TODO: think of returning value
  public BlipEvent insert(BlipEventDto blipEventDto) {
    BlipEvent savedBlipEvent = save(blipEventDto, false);
    blipEventRepository.updateBrothersToBeChildren(savedBlipEvent);
    return savedBlipEvent;
  }

  @Transactional
  public BlipEvent isolatedSave(BlipEventDto dto) {
    BlipEvent blipEvent = fillBlipEvent(dto);
    return blipEventRepository.save(blipEvent);
  }


  @Transactional(readOnly = true)
  public Collection<BlipEvent> find(Long blipEventId, Long blipId) {
    if (blipEventId == null || blipId == null) {
      return findAll();
    }
    return findBlipEventsOfTheBlip(blipId, blipEventId);
  }

  @Transactional(readOnly = true)
  public BlipEvent findById(Long id) {
    return blipEventRepository.findById(id).orElseThrow(() -> new NotFoundException(BlipEvent.class, id));
  }

  @Transactional(readOnly = true)
  public List<BlipEvent> findAll() {
    return blipEventRepository.findAll();
  }

  @Transactional(readOnly = true)
  public List<BlipEvent> findAllBlipsByBlipEventId(Long blipEventId) {
    return blipEventRepository.findAllBlipEventsByBlipEventId(blipEventId);
  }

  @Transactional(readOnly = true)
  public List<BlipEvent> findBlipEventsOfTheBlip(Long blipId, Long blipEventId) {
    return blipEventRepository.findBlipEventsOfTheBlip(blipId, blipEventId);
  }


  @Transactional
  public BlipEvent update(BlipEventDto dto, Boolean isMove) {
    if (isMove != null && isMove) {
      return move(dto);
    }
    return isolatedUpdate(dto);
  }

  @Transactional
  public BlipEvent move(BlipEventDto blipEventDto) {
    BlipEvent foundBlipEvent = findById(blipEventDto.getId());
    blipEventRepository.updateChildrenToBeBrothers(foundBlipEvent);
    BlipEvent updatedBlipEvent = update(blipEventDto, false);
    blipEventRepository.updateBrothersToBeChildren(updatedBlipEvent);
    return updatedBlipEvent;
  }

  @Transactional
  public BlipEvent isolatedUpdate(BlipEventDto dto) {
    Long id = dto.getId();
    BlipEvent entity = fillBlipEvent(dto);
    BlipEvent found = blipEventRepository.findById(id).orElseThrow(() -> new NotFoundException(BlipEvent.class, id));
    found.setLastChangeTime(Instant.now());
    return blipEventRepository.update(blipEventMapper.toUpdate(found, entity));
  }

  private BlipEvent fillBlipEvent(BlipEventDto dto) {
    BlipEvent blipEvent = blipEventMapper.toEntity(dto);
    Optional.ofNullable(dto.getParentId()).ifPresent(blipEvent::setParentId);
    blipEvent.setParentId(dto.getParentId());
    blipEvent.setBlip(blipService.findById(dto.getBlipId()));
    Optional.ofNullable(dto.getQuadrantId()).ifPresent(qId -> blipEvent.setQuadrant(quadrantService.findById(qId)));
    Optional.ofNullable(dto.getRingId()).ifPresent(rId -> blipEvent.setRing(ringService.findById(rId)));
    blipEvent.setUser(userService.findById(dto.getAuthorId()));
    return blipEvent;
  }

  @Transactional
  public void deleteById(Long id) {
    blipEventRepository.deleteById(id);
  }
  @Transactional
  public void deleteByIdChildrenPromoteToBrothers(Long blipEventId) {
    BlipEvent foundBlipEvent = findById(blipEventId);
    if(foundBlipEvent.getParentId() == null && blipEventRepository.findChildren(blipEventId).size() > 1) {
      throw new UnsupportedOperationException("Delete operation for root node with more then one child is not supported");
    }
    //TODO: make a good error message
    // (for now it's "deleted object would be re-saved by cascade (remove deleted object from associations):
    // [ru.hh.techradar.entity.BlipEvent#1]")
    // The problem is still in cycle dependencies: I cannot use radarVersionService here.
    deleteById(blipEventId);
    blipEventRepository.updateChildrenToBeBrothers(foundBlipEvent);
  }
}
