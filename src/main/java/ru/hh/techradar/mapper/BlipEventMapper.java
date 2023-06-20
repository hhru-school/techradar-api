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
    Optional.ofNullable(dto.getId()).ifPresent(blipEvent::setId);
    Optional.ofNullable(dto.getComment()).ifPresent(blipEvent::setComment);
    return blipEvent;
  }

  @Override
  public BlipEventDto toDto(BlipEvent entity) {
    BlipEventDto blipEventDto = new BlipEventDto();
    blipEventDto.setId(entity.getId());
    blipEventDto.setComment(entity.getComment());
    blipEventDto.setParentId(entity.getParentId());
    Optional.ofNullable(entity.getBlip()).ifPresent(b -> blipEventDto.setBlipId(entity.getBlip().getId()));
    Optional.ofNullable(entity.getQuadrant()).ifPresent(q -> blipEventDto.setQuadrantId(entity.getQuadrant().getId()));
    Optional.ofNullable(entity.getRing()).ifPresent(r -> blipEventDto.setRingId(entity.getRing().getId()));
    blipEventDto.setAuthorId(entity.getUser().getId());
    blipEventDto.setRadarId(entity.getRadar().getId());
    blipEventDto.setCreationTime(entity.getCreationTime());
    blipEventDto.setLastChangeTime(entity.getLastChangeTime());
    return blipEventDto;
  }
}
