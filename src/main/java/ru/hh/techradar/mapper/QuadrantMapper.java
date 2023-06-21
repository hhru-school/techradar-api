package ru.hh.techradar.mapper;

import java.util.Optional;
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
    QuadrantDto dto = new QuadrantDto();
    dto.setId(entity.getId());
    dto.setName(entity.getName());
    dto.setPosition(entity.getPosition());
    return dto;
  }

  public Quadrant update(Quadrant target, Quadrant source) {
    Optional.of(source.getRadar()).ifPresent(target::setRadar);
    Optional.ofNullable(source.getPosition()).ifPresent(target::setPosition);
    Optional.ofNullable(source.getName()).ifPresent(target::setName);
    return target;
  }
}
