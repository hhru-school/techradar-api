package ru.hh.techradar.mapper;

import java.util.Optional;
import org.springframework.stereotype.Component;
import ru.hh.techradar.dto.ContainerDto;
import ru.hh.techradar.entity.Container;

@Component
public class ContainerMapper extends AbstractMapper<Container, ContainerDto> {
  private final RadarMapper radarMapper;
  private final QuadrantMapper quadrantMapper;
  private final RingMapper ringMapper;
  private final BlipMapper blipMapper;

  public ContainerMapper(
      RadarMapper radarMapper,
      QuadrantMapper quadrantMapper,
      RingMapper ringMapper,
      BlipMapper blipMapper) {
    this.radarMapper = radarMapper;
    this.quadrantMapper = quadrantMapper;
    this.ringMapper = ringMapper;
    this.blipMapper = blipMapper;
  }

  @Override
  public Container toEntity(ContainerDto dto) {
    return null;
  }

  @Override
  public ContainerDto toDto(Container entity) {
    ContainerDto containerDto = new ContainerDto();
    Optional.ofNullable(entity.getBlipEvent()).ifPresent(be -> containerDto.setBlipEventId(be.getId()));
    containerDto.setRadar(radarMapper.toDto(entity.getRadar()));
    containerDto.setQuadrants(quadrantMapper.toDtos(entity.getQuadrants()));
    containerDto.setRings(ringMapper.toDtos(entity.getRings()));
    containerDto.setBlips(blipMapper.toDtos(entity.getBlips()));
    return containerDto;
  }
}
