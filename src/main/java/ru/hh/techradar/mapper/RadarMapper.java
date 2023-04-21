package ru.hh.techradar.mapper;

import java.util.Collection;
import java.util.List;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.stereotype.Component;
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
    return null;
  }

  @Override
  public RadarDto toDto(Radar entity) {
    RadarDto radarDto = new RadarDto();
    radarDto.setId(entity.getId());
    radarDto.setName(entity.getName());
    if (!(entity.getQuadrants() instanceof HibernateProxy)) {
      radarDto.setQuadrants(quadrantMapper.toDtos(entity.getQuadrants()));
    }
    if (!(entity.getRings() instanceof HibernateProxy)) {
      radarDto.setRings(ringMapper.toDtos(entity.getRings()));
    }
    if (!(entity.getBlips() instanceof HibernateProxy)) {
      radarDto.setBlips(blipMapper.toDtos(entity.getBlips()));
    }
    return radarDto;
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
