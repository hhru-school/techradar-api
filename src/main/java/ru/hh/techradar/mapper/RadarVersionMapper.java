package ru.hh.techradar.mapper;

import java.util.Optional;
import org.springframework.stereotype.Component;
import ru.hh.techradar.dto.RadarVersionDto;
import ru.hh.techradar.entity.RadarVersion;

@Component
public class RadarVersionMapper extends AbstractMapper<RadarVersion, RadarVersionDto> {

  @Override
  public RadarVersion toEntity(RadarVersionDto dto) {
    RadarVersion radarVersion = new RadarVersion();
    Optional.ofNullable(dto.getId()).ifPresent(radarVersion::setId);
    radarVersion.setName(dto.getName());
    radarVersion.setRelease(dto.getRelease());
    return radarVersion;
  }

  @Override
  public RadarVersionDto toDto(RadarVersion entity) {
    RadarVersionDto radarVersionDto = new RadarVersionDto();
    radarVersionDto.setId(entity.getId());
    radarVersionDto.setName(entity.getName());
    radarVersionDto.setRelease(entity.getRelease());
    radarVersionDto.setRadarId(entity.getRadar().getId());
    radarVersionDto.setBlipEventId(entity.getBlipEvent().getId());
    radarVersionDto.setLevel(entity.getLevel());
    Optional.ofNullable(entity.getParent()).ifPresent(p -> radarVersionDto.setParentId(p.getId()));
    Optional.ofNullable(entity.getParent()).ifPresent(p -> radarVersionDto.setParentName(p.getName()));
    radarVersionDto.setToggleAvailable(entity.getToggleAvailable());
    radarVersionDto.setCreationTime(entity.getCreationTime());
    radarVersionDto.setLastChangeTime(entity.getLastChangeTime());
    return radarVersionDto;
  }
}
