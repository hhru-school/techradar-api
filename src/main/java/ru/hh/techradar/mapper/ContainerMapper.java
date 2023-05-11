package ru.hh.techradar.mapper;

import org.springframework.stereotype.Component;
import ru.hh.techradar.dto.ContainerDto;
import ru.hh.techradar.entity.Container;

@Component
public class ContainerMapper extends AbstractMapper<Container, ContainerDto> {
  private final RadarMapper radarMapper;
  private final QuadrantMapper quadrantMapper;
  private final RingMapper ringMapper;
  private final BlipEventMapper blipEventMapper;
  private final BlipCreateMapper blipCreateMapper;
  private final BlipMapper blipMapper;

  public ContainerMapper(
      RadarMapper radarMapper,
      QuadrantMapper quadrantMapper,
      RingMapper ringMapper,
      BlipEventMapper blipEventMapper,
      BlipCreateMapper blipCreateMapper, BlipMapper blipMapper) {
    this.radarMapper = radarMapper;
    this.quadrantMapper = quadrantMapper;
    this.ringMapper = ringMapper;
    this.blipEventMapper = blipEventMapper;
    this.blipCreateMapper = blipCreateMapper;
    this.blipMapper = blipMapper;
  }

  @Override
  public Container toEntity(ContainerDto dto) {
    return null;
  }

  @Override
  public ContainerDto toDto(Container entity) {
    ContainerDto containerDto = new ContainerDto();
    containerDto.setBlipEventId(entity.getBlipEvent().getId());
    containerDto.setRadarId(entity.getRadar().getId());
    containerDto.setQuadrants(quadrantMapper.toDtos(entity.getQuadrants()));
    containerDto.setRings(ringMapper.toDtos(entity.getRings()));
    containerDto.setBlips(blipMapper.toDtos(entity.getBlips()));
    return containerDto;
  }
}
