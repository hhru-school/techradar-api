package ru.hh.techradar.mapper;

import java.util.Optional;
import org.springframework.stereotype.Component;
import ru.hh.techradar.dto.BlipCreateDto;
import ru.hh.techradar.dto.BlipDto;
import ru.hh.techradar.entity.Blip;

@Component
public class BlipMapper extends AbstractMapper<Blip, BlipDto> {

  public Blip toEntityFromCreateDto(BlipCreateDto dto) {
    Blip blip = new Blip();
    blip.setName(dto.getName());
    blip.setDescription(dto.getDescription());
    return blip;
  }
  @Override
  public Blip toEntity(BlipDto dto) {
    Blip blip = new Blip();
    blip.setId(dto.getId());
    blip.setName(dto.getName());
    blip.setDescription(dto.getDescription());
    return blip;
  }

  @Override
  public BlipDto toDto(Blip entity) {
    BlipDto blipDto = new BlipDto();
    blipDto.setId(entity.getId());
    blipDto.setName(entity.getName());
    blipDto.setDescription(entity.getDescription());
    blipDto.setQuadrantId(entity.getQuadrant().getId());
    blipDto.setRingId(entity.getRing().getId());
    blipDto.setRadarId(entity.getRadar().getId());
    blipDto.setRevisionNumber(entity.getRevisionNumber());
    return blipDto;
  }

  public Blip toUpdate(Blip target, Blip source) {
    Optional.ofNullable(source.getName()).ifPresent(target::setName);
    Optional.ofNullable(source.getDescription()).ifPresent(target::setDescription);
    Optional.ofNullable(source.getQuadrant()).ifPresent(target::setQuadrant);
    Optional.ofNullable(source.getRing()).ifPresent(target::setRing);
    Optional.ofNullable(source.getRadar()).ifPresent(target::setRadar);
    return target;
  }
}
