package ru.hh.techradar.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hh.techradar.dto.BlipEventDto;
import ru.hh.techradar.entity.Blip;
import ru.hh.techradar.entity.BlipEvent;
import ru.hh.techradar.entity.Radar;
import ru.hh.techradar.entity.RadarVersion;
import ru.hh.techradar.entity.User;
import ru.hh.techradar.exception.NotFoundException;
import ru.hh.techradar.exception.OperationNotAllowedException;
import ru.hh.techradar.mapper.BlipEventMapper;
import ru.hh.techradar.repository.BlipEventRepository;
import ru.hh.techradar.repository.RadarVersionRepository;

@Service
public class BlipEventService {
  public static final String ROOT_BLIP_EVENT_COMMENT = "root blip event";
  public static final String NO_CORRESPONDING_RELEASE_VERSION_EXCEPTION_MESSAGE = "There's no corresponding release version!";
  public static final String DELETE_OPERATION_FOR_ROOT_BLIP_EVENT_IS_NOT_SUPPORTED = "Delete operation for root blip event is not supported";
  private final BlipEventMapper blipEventMapper;
  private final BlipEventRepository blipEventRepository;
  private final BlipService blipService;
  private final QuadrantService quadrantService;
  private final RingService ringService;
  private final UserService userService;
  private final RadarService radarService;
  private final RadarVersionRepository radarVersionRepository;

  public BlipEventService(
      BlipEventMapper blipEventMapper, BlipEventRepository blipEventRepository,
      BlipService blipService,
      QuadrantService quadrantService,
      RingService ringService, UserService userService, RadarService radarService, RadarVersionRepository radarVersionRepository) {
    this.blipEventMapper = blipEventMapper;
    this.blipEventRepository = blipEventRepository;
    this.blipService = blipService;
    this.quadrantService = quadrantService;
    this.ringService = ringService;
    this.userService = userService;
    this.radarService = radarService;
    this.radarVersionRepository = radarVersionRepository;
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

  public BlipEvent prepareRootBlipEvent(User user, Radar radar) {
    return new BlipEvent(
        null,
        ROOT_BLIP_EVENT_COMMENT,
        null,
        null,
        null,
        user,
        null,
        null,
        null,
        radar
    );
  }

  @Transactional
  public BlipEvent saveRootBlipEvent(User user, Radar radar) {
    BlipEvent rootBlipEvent = prepareRootBlipEvent(user, radar);
    return blipEventRepository.save(rootBlipEvent);
  }

  @Transactional
  public List<BlipEvent> fillParentsAndSave(List<BlipEvent> blipEvents) {
    Long lastBlipEventId = null;
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

  @Transactional
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
  public Collection<BlipEvent> find(Long blipId) {
    if (blipId == null) {
      return findAll();
    }
    return findBlipEventsOfTheBlip(blipId);
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
    BlipEvent validatedExistence = findById(blipEventId);
    return blipEventRepository.findAllBlipEventsByBlipEventId(blipEventId);
  }

  @Transactional(readOnly = true)
  public List<BlipEvent> findBlipEventsOfTheBlip(Long blipId) {
    Blip blip = blipService.findById(blipId);
    RadarVersion radarVersion = radarVersionRepository.findLastReleasedRadarVersion(
        blip.getRadar().getId()).orElseThrow(() -> new NotFoundException(NO_CORRESPONDING_RELEASE_VERSION_EXCEPTION_MESSAGE)
    );
    return blipEventRepository.findBlipEventsOfTheBlip(blipId, radarVersion.getBlipEvent().getId());
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
    blipEvent.setRadar(radarService.findById(dto.getRadarId()));
    return blipEvent;
  }

  @Transactional
  public void deleteById(Long id) {
    BlipEvent foundBlipEvent = findById(id);
    throwExceptionIfBlipEventIsRoot(foundBlipEvent);
    blipEventRepository.deleteById(id);
  }

  @Transactional
  public void deleteByIdChildrenPromoteToBrothers(Long blipEventId) {
    BlipEvent foundBlipEvent = findById(blipEventId);
    throwExceptionIfBlipEventIsRoot(foundBlipEvent);
    BlipEvent parentBlipEvent = findById(foundBlipEvent.getParentId());
    radarVersionRepository.replaceBlipEventByItsParent(foundBlipEvent, parentBlipEvent);
    blipEventRepository.updateChildrenToBeBrothers(foundBlipEvent);
    deleteById(blipEventId);
  }

  private static void throwExceptionIfBlipEventIsRoot(BlipEvent foundBlipEvent) {
    if (foundBlipEvent.getParentId() == null) {
      throw new OperationNotAllowedException(DELETE_OPERATION_FOR_ROOT_BLIP_EVENT_IS_NOT_SUPPORTED);
    }
  }
}
