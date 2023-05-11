package ru.hh.techradar.mapper;

import java.util.Collection;
import java.util.List;
import org.springframework.stereotype.Component;
import ru.hh.techradar.dto.RadarDto;
import ru.hh.techradar.entity.Radar;

@Component
public class RadarMapper extends AbstractMapper<Radar, RadarDto> {

  @Override
  public Radar toEntity(RadarDto dto) {
    Radar radar = new Radar();
    radar.setName(dto.getName());
    return radar;
  }

  @Override
  public RadarDto toDto(Radar entity) {
    RadarDto radarDto = new RadarDto();
    radarDto.setId(entity.getId());
    radarDto.setName(entity.getName());
    return radarDto;
  }

  public RadarDto toShortDto(Radar entity) {
    RadarDto radarDto = new RadarDto();
    radarDto.setId(entity.getId());
    radarDto.setName(entity.getName());
    return radarDto;
  }

  public List<RadarDto> toShortDtos(Collection<Radar> entities) {
    return entities.stream().map(this::toShortDto).toList();
  }
}
