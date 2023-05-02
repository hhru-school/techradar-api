package ru.hh.techradar.mapper;

import org.springframework.stereotype.Component;
import ru.hh.techradar.dto.QuadrantDto;
import ru.hh.techradar.entity.Quadrant;

@Component
public class QuadrantMapper extends AbstractMapper<Quadrant, QuadrantDto> {
  @Override
  public Quadrant toEntity(QuadrantDto dto) {
    Quadrant quadrant = new Quadrant();
    quadrant.setName(dto.getName());
    quadrant.setPosition(dto.getPosition());
    return quadrant;
  }

  @Override
  public QuadrantDto toDto(Quadrant entity) {
    QuadrantDto quadrantDto = new QuadrantDto();
    quadrantDto.setId(entity.getId());
    quadrantDto.setName(entity.getName());
    quadrantDto.setPosition(entity.getPosition());
    return quadrantDto;
  }
}
