package ru.hh.techradar.mapper;

import org.springframework.stereotype.Component;
import ru.hh.techradar.dto.RingDto;
import ru.hh.techradar.entity.Ring;

@Component
public class RingMapper extends AbstractMapper<Ring, RingDto> {
  @Override
  public Ring toEntity(RingDto dto) {
//    Ring ring = new Ring();
//    RingSetting setting = new RingSetting(dto.getName(), dto.getPosition(), ring);
//    ring.setSettings(List.of(setting));
//    return ring;
    return null;
  }

  @Override
  public RingDto toDto(Ring entity) {
    RingDto dto = new RingDto();
    dto.setId(entity.getId());
    dto.setName(entity.getName());
    dto.setPosition(entity.getPosition());
    return dto;
  }



}
