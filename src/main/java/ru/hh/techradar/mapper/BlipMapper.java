package ru.hh.techradar.mapper;

import java.util.Optional;
import org.springframework.stereotype.Component;
import ru.hh.techradar.dto.BlipDto;
import ru.hh.techradar.entity.Blip;

@Component
public class BlipMapper extends AbstractMapper<Blip, BlipDto> {
  private final QuadrantMapper quadrantMapper;
  private final RingMapper ringMapper;

  public BlipMapper(QuadrantMapper quadrantMapper, RingMapper ringMapper) {
    this.quadrantMapper = quadrantMapper;
    this.ringMapper = ringMapper;
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
    blipDto.setQuadrant(quadrantMapper.toDto(entity.getBlipEvents().get(0).getQuadrant()));
    blipDto.setRing(ringMapper.toDto(entity.getBlipEvents().get(0).getRing()));
    return blipDto;
  }

  @Override
  public Blip toUpdate(Blip target, Blip source) {
    Optional.ofNullable(source.getName()).ifPresent(target::setName);
    Optional.ofNullable(source.getDescription()).ifPresent(target::setDescription);
    Optional.ofNullable(source.getRadar()).ifPresent(target::setRadar);
    return target;
  }
}
