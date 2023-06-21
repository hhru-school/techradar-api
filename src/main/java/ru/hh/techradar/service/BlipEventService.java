package ru.hh.techradar.service;

import jakarta.validation.Validator;
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
  private final BlipEventRepository blipEventRepository;
  private final BlipService blipService;
  private final QuadrantService quadrantService;
  private final RingService ringService;
  private final UserService userService;
  private final RadarService radarService;
  private final RadarVersionRepository radarVersionRepository;
  private final BlipEventMapper blipEventMapper;
  private final Validator validator;

  public BlipEventService(
      BlipEventRepository blipEventRepository,
      BlipService blipService,
      QuadrantService quadrantService,
      RingService ringService,
      UserService userService,
      RadarService radarService,
      RadarVersionRepository radarVersionRepository, BlipEventMapper blipEventMapper, Validator validator) {
    this.blipEventRepository = blipEventRepository;
    this.blipService = blipService;
    this.quadrantService = quadrantService;
    this.ringService = ringService;
    this.userService = userService;
    this.radarService = radarService;
    this.radarVersionRepository = radarVersionRepository;
    this.blipEventMapper = blipEventMapper;
    this.validator = validator;
  }

  @Transactional
  public BlipEvent save(BlipEvent blipEvent) {
    return blipEventRepository.save(blipEvent);
  }

  @Transactional
  public BlipEvent save(String username, BlipEventDto dto, Boolean isInsert) {
    if (isInsert != null && isInsert) {
      return insert(dto, username);
    }
    return isolatedSave(dto, username);
  }

  @Transactional
  public BlipEvent saveForVersion(String username, BlipEventDto dto, Long radarVersionId) {
    RadarVersion radarVersion = radarVersionRepository.findById(radarVersionId)
        .orElseThrow(() -> new NotFoundException(RadarVersion.class, radarVersionId));
    RadarVersion parentRadarVersion = radarVersionRepository.getParentRadarVersion(radarVersion)
        .orElseThrow(() -> new NotFoundException(RadarVersion.class, radarVersion.getParent().getId()));
    if (!parentRadarVersion.getBlipEvent().getId()
        .equals(radarVersion.getBlipEvent().getId())) {
      return insert(dto, username);
    }
    return isolatedSave(dto, username);
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
  public BlipEvent insert(BlipEventDto blipEventDto, String username) {
    BlipEvent savedBlipEvent = save(username, blipEventDto, false);
    blipEventRepository.updateBrothersToBeChildren(savedBlipEvent);
    return savedBlipEvent;
  }

  @Transactional
  public BlipEvent isolatedSave(BlipEventDto dto, String username) {
    BlipEvent blipEvent = fillBlipEvent(dto, username);
    validator.validate(blipEvent);
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
  public List<BlipEvent> findAllBlipsByRadarVersionId(Long radarVersionId) {
    RadarVersion radarVersion = radarVersionRepository.findById(radarVersionId).orElseThrow(() -> new NotFoundException(
        RadarVersion.class,
        radarVersionId
    ));
    BlipEvent blipEvent = radarVersion.getBlipEvent();
    return blipEventRepository.findAllBlipEventsByBlipEventId(blipEvent.getId());
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
    validator.validate(updatedBlipEvent);
    return updatedBlipEvent;
  }

  @Transactional
  public BlipEvent isolatedUpdate(BlipEventDto dto) {
    Long id = dto.getId();
    BlipEvent target = blipEventRepository.findById(id).orElseThrow(() -> new NotFoundException(BlipEvent.class, id));
    fillBlipEventForUpdate(target, dto);
    validator.validate(target);
    return blipEventRepository.update(target);
  }

  private BlipEvent fillBlipEvent(BlipEventDto dto, String username) {
    BlipEvent blipEvent = blipEventMapper.toEntity(dto);
    Optional.ofNullable(dto.getParentId()).ifPresent(blipEvent::setParentId);
    blipEvent.setBlip(blipService.findById(dto.getBlipId()));
    Optional.ofNullable(dto.getQuadrantId()).ifPresent(qId -> blipEvent.setQuadrant(qId.map(quadrantService::findById).orElse(null)));
    Optional.ofNullable(dto.getRingId()).ifPresent(rId -> blipEvent.setRing(rId.map(ringService::findById).orElse(null)));
    blipEvent.setUser(userService.findByUsername(username));
    blipEvent.setRadar(radarService.findById(dto.getRadarId()));
    return blipEvent;
  }

  private void fillBlipEventForUpdate(BlipEvent target, BlipEventDto dto) {
    Optional.ofNullable(dto.getComment()).ifPresent(target::setComment);
    Optional.ofNullable(dto.getParentId()).ifPresent(target::setParentId);
    Optional.ofNullable(dto.getBlipId()).ifPresent(bId -> target.setBlip(blipService.findById(bId)));
    Optional.ofNullable(dto.getQuadrantId()).ifPresent(qId -> target.setQuadrant(qId.map(quadrantService::findById).orElse(null)));
    Optional.ofNullable(dto.getRingId()).ifPresent(rId -> target.setRing(rId.map(ringService::findById).orElse(null)));
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
