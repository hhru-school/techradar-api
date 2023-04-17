package ru.hh.techradar.mapper;

import java.util.Optional;
import org.springframework.stereotype.Component;
import ru.hh.techradar.dto.BlipEventDto;
import ru.hh.techradar.entity.BlipEvent;

@Component
public class BlipEventMapper extends AbstractMapper<BlipEvent, BlipEventDto> {

  @Override
  public BlipEvent toEntity(BlipEventDto dto) {
    BlipEvent blipEvent = new BlipEvent();
    blipEvent.setId(dto.getId());
    blipEvent.setComment(dto.getComment());
    blipEvent.setVersionName(dto.getVersionName());
    return blipEvent;
  }

  @Override
  public BlipEventDto toDto(BlipEvent entity) {
    BlipEventDto blipEventDto = new BlipEventDto();
    blipEventDto.setId(entity.getId());
    blipEventDto.setComment(entity.getComment());
    blipEventDto.setVersionName(entity.getVersionName());
    blipEventDto.setCreationTime(entity.getCreationTime());
    blipEventDto.setLastChangeTime(entity.getLastChangeTime());
    return blipEventDto;
  }

  @Override
  public BlipEvent toUpdate(BlipEvent target, BlipEvent source) {
    Optional.ofNullable(source.getComment()).ifPresent(target::setComment);
    Optional.ofNullable(source.getVersionName()).ifPresent(target::setVersionName);
    Optional.ofNullable(source.getBlip()).ifPresent(target::setBlip);
    Optional.ofNullable(source.getQuadrant()).ifPresent(target::setQuadrant);
    Optional.ofNullable(source.getRing()).ifPresent(target::setRing);
    Optional.ofNullable(source.getUser()).ifPresent(target::setUser);
    return target;
  }
}
