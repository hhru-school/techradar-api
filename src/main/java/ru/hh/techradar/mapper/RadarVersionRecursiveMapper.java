package ru.hh.techradar.mapper;

import java.util.Optional;
import org.springframework.stereotype.Component;
import ru.hh.techradar.dto.RadarVersionRecursiveDto;
import ru.hh.techradar.entity.RadarVersion;

@Component
public class RadarVersionRecursiveMapper extends AbstractMapper<RadarVersion, RadarVersionRecursiveDto> {
  @Override
  public RadarVersion toEntity(RadarVersionRecursiveDto dto) {
    return null;
  }

  @Override
  public RadarVersionRecursiveDto toDto(RadarVersion entity) {
    RadarVersionRecursiveDto radarVersionRecursiveDto = new RadarVersionRecursiveDto();
    radarVersionRecursiveDto.setId(entity.getId());
    radarVersionRecursiveDto.setName(entity.getName());
    radarVersionRecursiveDto.setRelease(entity.getRelease());
    radarVersionRecursiveDto.setRadarId(entity.getRadar().getId());
    radarVersionRecursiveDto.setBlipEventId(entity.getBlipEvent().getId());
    radarVersionRecursiveDto.setLevel(entity.getLevel());
    Optional.ofNullable(entity.getParent()).ifPresent(p -> radarVersionRecursiveDto.setParentId(p.getId()));
    radarVersionRecursiveDto.setToggleAvailable(entity.getToggleAvailable());
    radarVersionRecursiveDto.setCreationTime(entity.getCreationTime());
    radarVersionRecursiveDto.setLastChangeTime(entity.getLastChangeTime());
    radarVersionRecursiveDto.setChildren(toDtos(entity.getChildren()));
    return radarVersionRecursiveDto;
  }
}
