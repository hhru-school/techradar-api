package ru.hh.techradar.service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hh.techradar.dto.RadarVersionDto;
import ru.hh.techradar.entity.BlipEvent;
import ru.hh.techradar.entity.Container;
import ru.hh.techradar.entity.Radar;
import ru.hh.techradar.entity.RadarVersion;
import ru.hh.techradar.exception.EntityExistsException;
import ru.hh.techradar.exception.NotFoundException;
import ru.hh.techradar.exception.OperationNotAllowedException;
import ru.hh.techradar.mapper.RadarVersionMapper;
import ru.hh.techradar.repository.RadarVersionRepository;

@Service
public class RadarVersionService {
  public static final String CREATION_OF_RELEASED_VERSION_IS_NOT_ALLOWED = "You can't create released version: this operation available for " +
      "updating only!";
  public static final String NAME_OF_ROOT_RADAR_VERSION = "_init_";
  public static final String NO_RELEASE_VERSIONS_FOR_RADAR = "There's no release versions for radar ";
  public static final String MODIFICATION_OF_ROOT_VERSION_IS_FORBIDDEN = "You can't modify root version!";
  public static final String RELEASE_STATUS_TOGGLE_IS_FORBIDDEN = "Change release status of this version is not allowed: " +
      "it's parent is not a release version or there's release version on the same level already";
  public static final String ROOT_VERSION_REMOVE_IS_FORBIDDEN = "You can't delete root version!";
  public static final String RADAR_VERSION_WITH_CHILDREN_REMOVAL_IS_FORBIDDEN = "Radar version with children can't be deleted";
  private final RadarVersionRepository radarVersionRepository;
  private final BlipEventService blipEventService;
  private final RadarVersionMapper radarVersionMapper;
  private final RadarService radarService;

  public RadarVersionService(
      RadarVersionRepository radarVersionRepository,
      BlipEventService blipEventService,
      RadarVersionMapper radarVersionMapper, RadarService radarService) {
    this.radarVersionRepository = radarVersionRepository;
    this.blipEventService = blipEventService;
    this.radarVersionMapper = radarVersionMapper;
    this.radarService = radarService;
  }

  @Transactional
  public RadarVersion saveReleaseVersion(Container container, String versionName) {
    RadarVersion radarVersion = new RadarVersion();
    radarVersion.setRelease(true);
    radarVersion.setRadar(container.getRadar());
    radarVersion.setName(versionName);
    radarVersion.setBlipEvent(container.getBlipEvent());
    radarVersion.setParent(container.getRadarVersion());
    radarVersion.setLevel(container.getRadarVersion().getLevel() + 1);
    radarVersion.setToggleAvailable(true);
    return radarVersionRepository.save(radarVersion);
  }

  @Transactional
  public RadarVersion save(RadarVersionDto dto, Boolean toLastRelease) {
    RadarVersion radarVersion = radarVersionMapper.toEntity(dto);
    Optional.ofNullable(dto.getBlipEventId()).ifPresent(beId -> radarVersion.setBlipEvent(blipEventService.findById(beId)));
    radarVersion.setRadar(radarService.findById(dto.getRadarId()));
    if (radarVersionRepository.findByNameAndRadarId(radarVersion.getName(), radarVersion.getRadar().getId()).isPresent()) {
      throw new EntityExistsException(RadarVersion.class, radarVersion.getName());
    }
    RadarVersion lastRadarVersion = findLastReleasedRadarVersion(radarVersion.getRadar().getId());
    BlipEvent blipEvent = radarVersion.getBlipEvent();
    if (toLastRelease != null && toLastRelease) {
      blipEvent = lastRadarVersion.getBlipEvent();
    } else if (radarVersion.getRelease() != null && radarVersion.getRelease()) {
      throw new OperationNotAllowedException(CREATION_OF_RELEASED_VERSION_IS_NOT_ALLOWED);
    }
    return saveNextRadarVersion(radarVersion, blipEvent);
  }

  @Transactional
  public RadarVersion saveRootRadarVersion(Radar radar, BlipEvent rootBlipEvent) {
    RadarVersion rootRadarVersion = new RadarVersion(
        null,
        null,
        null,
        NAME_OF_ROOT_RADAR_VERSION,
        true,
        radar,
        rootBlipEvent,
        null,
        0,
        true
    );
    return radarVersionRepository.save(rootRadarVersion);
  }

  @Transactional
  public RadarVersion saveNextRadarVersion(RadarVersion radarVersionToSave, BlipEvent blipEvent) {
    RadarVersion lastRadarVersion = findLastReleasedRadarVersion(radarVersionToSave.getRadar().getId());
    radarVersionToSave.setRelease(false);
    radarVersionToSave.setBlipEvent(blipEvent);
    radarVersionToSave.setParent(lastRadarVersion);
    int level = lastRadarVersion.getLevel() + 1;
    radarVersionToSave.setLevel(level);
    radarVersionToSave.setToggleAvailable(!checkIfReleasedVersionOnLevel(level, radarVersionToSave.getRadar().getId()));
    return radarVersionRepository.save(radarVersionToSave);
  }

  @Transactional(readOnly = true)
  public Boolean checkIfReleasedVersionOnLevel(Integer level, Long radarId) {
    return radarVersionRepository.checkIfReleasedVersionOnLevel(level, radarId);
  }

  @Transactional(readOnly = true)
  public RadarVersion findById(Long id) {
    return radarVersionRepository.findById(id).orElseThrow(() -> new NotFoundException(RadarVersion.class, id));
  }

  @Transactional(readOnly = true)
  public RadarVersion findRoot(Long radarId) {
    return radarVersionRepository.findRoot(radarId).orElseThrow(() -> new NotFoundException(
        RadarVersion.class,
        radarId));//TODO: change to correct error (now it's radarId instead of real id)
  }

  @Transactional(readOnly = true)
  public List<RadarVersion> findAll() {
    return radarVersionRepository.findAll();
  }

  @Transactional(readOnly = true)
  public Collection<RadarVersion> findByRadarId(Long radarId) {
    return radarVersionRepository.findAllByRadarId(radarId);
  }

  @Transactional(readOnly = true)
  public RadarVersion findLastReleasedRadarVersion(Long radarId) {
    return radarVersionRepository.findLastReleasedRadarVersion(radarId)
        .orElseThrow(() -> new NotFoundException(NO_RELEASE_VERSIONS_FOR_RADAR + radarId));
  }

  @Transactional(readOnly = true)
  public Optional<RadarVersion> findLastRadarVersionGracefully(Long radarId) {
    return radarVersionRepository.findLastReleasedRadarVersion(radarId);
  }

  @Transactional
  public RadarVersion update(Long id, RadarVersionDto dto) {
    RadarVersion target = findById(id);
    updateValidating(dto, target);
    updateReleaseAndToggle(dto, target);
    Optional.ofNullable(dto.getName()).ifPresent(target::setName);
    updateBlipEvent(dto, target);
    RadarVersion updatedRadarVersion = radarVersionRepository.update(target);
    Hibernate.initialize(updatedRadarVersion.getParent());
    return updatedRadarVersion;
  }

  private void updateValidating(RadarVersionDto dto, RadarVersion target) {
    if (target.getLevel() == 0) {
      throw new OperationNotAllowedException(MODIFICATION_OF_ROOT_VERSION_IS_FORBIDDEN);
    }
    if (dto.getName() != null && !target.getName().equals(dto.getName()) &&
        radarVersionRepository.findByNameAndRadarId(dto.getName(), target.getRadar().getId()).isPresent()) {
      throw new EntityExistsException(RadarVersion.class, dto.getName());
    }
  }

  private void updateReleaseAndToggle(RadarVersionDto dto, RadarVersion target) {
    Optional.ofNullable(dto.getRelease()).ifPresent(isRelease -> {
      if (!isRelease.equals(target.getRelease())) {
        if (!target.getToggleAvailable()) {
          throw new OperationNotAllowedException(RELEASE_STATUS_TOGGLE_IS_FORBIDDEN);
        }
        radarVersionRepository.setToggleAvailabilityOfRelatives(target.getParent().getId(), !isRelease);
        target.setToggleAvailable(true);
        target.setRelease(isRelease);
        radarVersionRepository.setToggleAvailabilityOfRelatives(target.getId(), isRelease);
        radarVersionRepository.setToggleAvailability(target.getParent().getId(), !isRelease);
      }
    });
  }

  private void updateBlipEvent(RadarVersionDto dto, RadarVersion target) {
    Optional.ofNullable(dto.getBlipEventId())
        .ifPresent(beId -> {
          BlipEvent blipEventToSet = blipEventService.findById(beId);
          if (!target.getBlipEvent().equals(blipEventToSet)) {
            radarVersionRepository.setBlipEventToChildrenWhereBlipEventEqualsToParents(target, blipEventToSet);
            target.setBlipEvent(blipEventToSet);
          }
        });
  }

  @Transactional
  public void deleteById(Long id) {
    if (findById(id).getLevel().equals(0)) {
      throw new OperationNotAllowedException(ROOT_VERSION_REMOVE_IS_FORBIDDEN);
    }
    if (!radarVersionRepository.findChildren(id).isEmpty()) {
      throw new OperationNotAllowedException(RADAR_VERSION_WITH_CHILDREN_REMOVAL_IS_FORBIDDEN);
    }
    radarVersionRepository.deleteById(id);
  }

  @Transactional(readOnly = true)
  public Collection<RadarVersion> findAllReleasedRadarVersions(Long radarId) {
    return radarVersionRepository.findAllReleasedRadarVersions(radarId);
  }
}
