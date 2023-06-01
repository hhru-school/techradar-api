package ru.hh.techradar.mapper;

import org.springframework.stereotype.Component;
import ru.hh.techradar.dto.BlipCreateDto;
import ru.hh.techradar.entity.Blip;

@Component
public class BlipCreateMapper extends AbstractMapper<Blip, BlipCreateDto> {
  @Override
  public Blip toEntity(BlipCreateDto dto) {
    Blip blip = new Blip();
    blip.setName(dto.getName());
    blip.setDescription(dto.getDescription());
    return blip;
  }

  @Override
  public BlipCreateDto toDto(Blip entity) {
    return null;
  }
}
