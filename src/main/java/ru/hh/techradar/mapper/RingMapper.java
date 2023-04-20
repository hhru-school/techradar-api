package ru.hh.techradar.mapper;

import java.util.Collection;
import java.util.List;
import org.springframework.stereotype.Component;
import ru.hh.techradar.dto.RingDto;
import ru.hh.techradar.entity.Ring;
import ru.hh.techradar.entity.RingSetting;

@Component
public class RingMapper implements BaseMapper<Ring, RingDto> {
  @Override
  public Ring toEntity(RingDto dto) {
    Ring ring = new Ring();
    RingSetting setting = new RingSetting(dto.getName(), dto.getPosition(), ring);
    ring.setSettings(List.of(setting));
    return ring;
  }

  @Override
  public RingDto toDto(Ring entity) {
    RingDto dto = new RingDto();
    dto.setId(entity.getId());
    dto.setName(entity.getCurrentSetting().getName());
    dto.setPosition(entity.getCurrentSetting().getPosition());
    return dto;
  }

  @Override
  public List<RingDto> toDtos(Collection<Ring> entities) {
    return entities.stream().map(this::toDto).toList();
  }

  @Override
  public List<Ring> toEntities(Collection<RingDto> dtos) {
    return dtos.stream().map(this::toEntity).toList();
  }

}
