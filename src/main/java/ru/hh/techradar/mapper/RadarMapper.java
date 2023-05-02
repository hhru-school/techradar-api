package ru.hh.techradar.mapper;

import java.util.Collection;
import java.util.List;
import org.springframework.stereotype.Component;
import ru.hh.techradar.dto.RadarCreateDto;
import ru.hh.techradar.dto.RadarDto;
import ru.hh.techradar.dto.RadarShortDto;
import ru.hh.techradar.entity.Radar;

@Component
public class RadarMapper extends AbstractMapper<Radar, RadarDto> {

  private final QuadrantMapper quadrantMapper;
  private final RingMapper ringMapper;
  private final BlipMapper blipMapper;

  public RadarMapper(QuadrantMapper quadrantMapper, RingMapper ringMapper, BlipMapper blipMapper) {
    this.quadrantMapper = quadrantMapper;
    this.ringMapper = ringMapper;
    this.blipMapper = blipMapper;
  }

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
    radarDto.setAuthorId(entity.getAuthor().getId());
    radarDto.setCompanyId(entity.getCompany().getId());
    radarDto.setQuadrants(quadrantMapper.toDtos(entity.getQuadrants()));
    radarDto.setRings(ringMapper.toDtos(entity.getRings()));
    radarDto.setBlips(blipMapper.toDtos(entity.getBlips()));
    return radarDto;
  }

  public Radar toEntityFromCreateDto(RadarCreateDto dto) {
    Radar radar = new Radar();
    radar.setName(dto.getName());
    return radar;
  }

  public RadarShortDto toShortDto(Radar entity) {
    RadarShortDto radarShortDto = new RadarShortDto();
    radarShortDto.setId(entity.getId());
    radarShortDto.setName(entity.getName());
    return radarShortDto;
  }

  public List<RadarShortDto> toShortDtos(Collection<Radar> entities) {
    return entities.stream().map(this::toShortDto).toList();
  }

  public Radar toUpdate(Radar target, Radar source) {
    return null;
  }
}
