package ru.hh.techradar.mapper;

import org.springframework.stereotype.Component;
import ru.hh.techradar.dto.BlipTemplateDto;
import ru.hh.techradar.entity.BlipTemplate;

@Component
public class BlipTemplateMapper extends AbstractMapper<BlipTemplate, BlipTemplateDto> {
  @Override
  public BlipTemplate toEntity(BlipTemplateDto dto) {
    return new BlipTemplate(dto.getName(), dto.getDescription());
  }

  @Override
  public BlipTemplateDto toDto(BlipTemplate entity) {
    return new BlipTemplateDto(entity.getName(), entity.getDescription());
  }

  public BlipTemplate update(BlipTemplate source, BlipTemplate target) {
    target.setDescription(source.getDescription());
    return target;
  }
}
