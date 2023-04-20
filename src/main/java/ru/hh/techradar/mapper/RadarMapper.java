package ru.hh.techradar.mapper;

import org.springframework.stereotype.Component;
import ru.hh.techradar.dto.RadarDto;
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
    return null;
  }

  @Override
  public RadarDto toDto(Radar entity) {
    RadarDto radarDto = new RadarDto();
    radarDto.setId(entity.getId());
    radarDto.setName(entity.getName());
    radarDto.setQuadrants(quadrantMapper.toDtos(entity.getQuadrants()));
    radarDto.setRings(ringMapper.toDtos(entity.getRings()));
    radarDto.setBlips(blipMapper.toDtos(entity.getBlips()));
    return radarDto;
  }

  @Override
  public Radar toUpdate(Radar target, Radar source) {
    return null;
  }
}
