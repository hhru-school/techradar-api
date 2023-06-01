package ru.hh.techradar.mapper;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;
import ru.hh.techradar.dto.RadarDto;
import ru.hh.techradar.entity.Radar;

@Component
public class RadarMapper extends AbstractMapper<Radar, RadarDto> {

  //TODO: think of adding company_id and user_id
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
    radarDto.setCompanyId(entity.getCompany().getId());
    radarDto.setAuthorId(entity.getAuthor().getId());
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

  public Radar toUpdate(Radar target, Radar source) {
    Optional.ofNullable(source.getName()).ifPresent(target::setName);
    Optional.ofNullable(source.getCompany()).ifPresent(target::setCompany);
    Optional.ofNullable(source.getAuthor()).ifPresent(target::setAuthor);
    return target;
  }
}