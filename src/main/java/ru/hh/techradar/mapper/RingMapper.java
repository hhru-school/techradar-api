package ru.hh.techradar.mapper;

import java.util.Optional;
import org.springframework.stereotype.Component;
import ru.hh.techradar.dto.RingDto;
import ru.hh.techradar.entity.Ring;

@Component
public class RingMapper extends AbstractMapper<Ring, RingDto> {
  @Override
  public Ring toEntity(RingDto dto) {
    Ring ring = new Ring();
    ring.setName(dto.getName());
    ring.setPosition(dto.getPosition());
    return ring;
  }

  @Override
  public RingDto toDto(Ring entity) {
    RingDto dto = new RingDto();
    dto.setId(entity.getId());
    dto.setName(entity.getName());
    dto.setPosition(entity.getPosition());
    return dto;
  }

  public Ring update(Ring target, Ring source) {
    Optional.ofNullable(source.getRadar()).ifPresent(target::setRadar);
    Optional.ofNullable(source.getPosition()).ifPresent(target::setPosition);
    Optional.ofNullable(source.getName()).ifPresent(target::setName);
    return target;
  }
}
