package ru.hh.techradar.mapper;

import java.util.List;
import org.springframework.stereotype.Component;
import ru.hh.techradar.dto.QuadrantDto;
import ru.hh.techradar.entity.Quadrant;
import ru.hh.techradar.entity.QuadrantSetting;

@Component
public class QuadrantMapper extends AbstractMapper<Quadrant, QuadrantDto> {
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
    dto.setRadarId(entity.getRadar().getId());
    return dto;
  }
}
