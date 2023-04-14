package ru.hh.techradar.mapper;

import java.util.Collection;
import java.util.List;
import org.springframework.stereotype.Component;
import ru.hh.techradar.dto.QuadrantDto;
import ru.hh.techradar.entity.Quadrant;
import ru.hh.techradar.entity.QuadrantSetting;

@Component
public class QuadrantMapper implements BaseMapper<Quadrant, QuadrantDto> {
  @Override
  public Quadrant toEntity(QuadrantDto dto) {
    Quadrant quadrant = new Quadrant();
    QuadrantSetting setting = new QuadrantSetting(dto.getName(), dto.getPosition(), quadrant);
    quadrant.setSettings(List.of(setting));
    return quadrant;
  }

  @Override
  public QuadrantDto toDto(Quadrant entity) {
    QuadrantDto dto = new QuadrantDto();
    dto.setId(entity.getId());
    dto.setName(entity.getCurrentSetting().getName());
    dto.setPosition(entity.getCurrentSetting().getPosition());
    return dto;
  }

  @Override
  public List<QuadrantDto> toDtos(Collection<Quadrant> entities) {
    return entities.stream().map(this::toDto).toList();
  }

  @Override
  public List<Quadrant> toEntities(Collection<QuadrantDto> dtos) {
    return dtos.stream().map(this::toEntity).toList();
  }
}
