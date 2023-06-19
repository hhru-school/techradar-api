package ru.hh.techradar.mapper;

import java.util.Optional;
import org.springframework.stereotype.Component;
import ru.hh.techradar.dto.BlipEventShortReadDto;
import ru.hh.techradar.entity.BlipEvent;

@Component
public class BlipEventShortReadMapper extends AbstractMapper<BlipEvent, BlipEventShortReadDto>{

  @Override
  public BlipEvent toEntity(BlipEventShortReadDto dto) {
    return null;
  }

  @Override
  public BlipEventShortReadDto toDto(BlipEvent entity) {
    BlipEventShortReadDto blipEventShortReadDto = new BlipEventShortReadDto();
    blipEventShortReadDto.setId(entity.getId());
    blipEventShortReadDto.setComment(entity.getComment());
    blipEventShortReadDto.setParentId(entity.getParentId());
    Optional.ofNullable(entity.getBlip()).ifPresent(b -> blipEventShortReadDto.setBlipId(entity.getBlip().getId()));
    Optional.ofNullable(entity.getQuadrant()).ifPresent(q -> blipEventShortReadDto.setQuadrantId(entity.getQuadrant().getId()));
    Optional.ofNullable(entity.getRing()).ifPresent(r -> blipEventShortReadDto.setRingId(entity.getRing().getId()));
    blipEventShortReadDto.setAuthorId(entity.getUser().getId());
    blipEventShortReadDto.setRadarId(entity.getRadar().getId());
    blipEventShortReadDto.setCreationTime(entity.getCreationTime());
    blipEventShortReadDto.setLastChangeTime(entity.getLastChangeTime());
    return blipEventShortReadDto;
  }
}
