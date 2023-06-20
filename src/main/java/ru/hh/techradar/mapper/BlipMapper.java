package ru.hh.techradar.mapper;

import java.util.Optional;
import org.springframework.stereotype.Component;
import ru.hh.techradar.dto.BlipDto;
import ru.hh.techradar.entity.Blip;

@Component
public class BlipMapper extends AbstractMapper<Blip, BlipDto> {

  @Override
  public Blip toEntity(BlipDto dto) {
    Blip blip = new Blip();
    Optional.ofNullable(dto.getId()).ifPresent(blip::setId);
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
    blipDto.setQuadrantId(entity.getQuadrantId());
    blipDto.setRingId(entity.getRingId());
    blipDto.setRadarId(entity.getRadar().getId());
    Optional.ofNullable(entity.getDrawInfo()).ifPresent(blipDto::setDrawInfo);
    return blipDto;
  }

  public Blip toUpdate(Blip target, Blip source) {
    Optional.ofNullable(source.getName()).ifPresent(target::setName);
    Optional.ofNullable(source.getDescription()).ifPresent(target::setDescription);
    Optional.ofNullable(source.getRadar()).ifPresent(target::setRadar);
    return target;
  }
}
