package ru.hh.techradar.mapper;

import java.util.Optional;
import org.springframework.stereotype.Component;
import ru.hh.techradar.dto.BlipEventReadDto;
import ru.hh.techradar.entity.BlipEvent;

@Component
public class BlipEventReadMapper extends AbstractMapper<BlipEvent, BlipEventReadDto> {
  private final RingMapper ringMapper;
  private final QuadrantMapper quadrantMapper;
  private final BlipMapper blipMapper;
  private final UserMapper userMapper;

  public BlipEventReadMapper(RingMapper ringMapper, QuadrantMapper quadrantMapper, BlipMapper blipMapper, UserMapper userMapper) {
    this.ringMapper = ringMapper;
    this.quadrantMapper = quadrantMapper;
    this.blipMapper = blipMapper;
    this.userMapper = userMapper;
  }

  @Override
  public BlipEvent toEntity(BlipEventReadDto dto) {
    return null;
  }

  @Override
  public BlipEventReadDto toDto(BlipEvent entity) {
    BlipEventReadDto blipEventReadDto = new BlipEventReadDto();
    blipEventReadDto.setId(entity.getId());
    blipEventReadDto.setComment(entity.getComment());
    blipEventReadDto.setParentId(entity.getParentId());
    Optional.ofNullable(entity.getBlip()).ifPresent(b -> blipEventReadDto.setBlip(blipMapper.toDto(b)));
    Optional.ofNullable(entity.getQuadrant()).ifPresent(q -> blipEventReadDto.setQuadrant(quadrantMapper.toDto(q)));
    Optional.ofNullable(entity.getRing()).ifPresent(r -> blipEventReadDto.setRing(ringMapper.toDto(r)));
    blipEventReadDto.setAuthor(userMapper.toDto(entity.getUser()));
    blipEventReadDto.setCreationTime(entity.getCreationTime());
    blipEventReadDto.setLastChangeTime(entity.getLastChangeTime());
    return blipEventReadDto;
  }
}
