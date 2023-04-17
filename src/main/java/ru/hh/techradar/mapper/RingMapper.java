package ru.hh.techradar.mapper;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import org.springframework.stereotype.Component;
import ru.hh.techradar.dto.RingDto;
import ru.hh.techradar.entity.Radar;
import ru.hh.techradar.entity.Ring;
import ru.hh.techradar.entity.RingSetting;
import ru.hh.techradar.exception.NotFoundException;
import ru.hh.techradar.repository.RadarRepository;
import ru.hh.techradar.repository.RingRepository;
import ru.hh.techradar.service.RadarService;
import ru.hh.techradar.service.RingSettingService;

@Component
public class RingMapper implements BaseMapper<Ring, RingDto> {
  private final RingSettingService ringSettingService;
  private final RadarService radarService;
  public RingMapper(RadarService radarService, RingSettingService ringSettingService) {
    this.radarService = radarService;
    this.ringSettingService = ringSettingService;
  }
  @Deprecated
  @Override
  public Ring toEntity(RingDto dto) {
    throw new UnsupportedOperationException();
  }

  public Ring toEntity(RingDto dto, Long radarId) {
    Radar radar = radarService.findById(radarId);
    var ring = new Ring(
        dto.getId(),
        radar,
        dto.getCreationTime(),
        dto.getLastChangeTime(),
        dto.getRemovedAt()
    );
    RingSetting ringSetting = new RingSetting(
        dto.getName(),
        dto.getPosition(),
        ring,
        Instant.now()
    );
    if (dto.getId() != null) {
      ringSettingService.save(ringSetting);
    }
    ring.getSettings().add(ringSetting);
    return ring;
  }

  @Deprecated
  @Override
  public RingDto toDto(Ring entity) {
    throw new UnsupportedOperationException();
  }

  public RingDto toDtoByDate(Ring entity, Instant date) {
    RingSetting ringSetting = ringSettingService.findRingSettingByDate(entity, date).orElseThrow(() -> new NotFoundException(Ring.class, date));
    return new RingDto(
        entity.getId(),
        ringSetting.getName(),
        ringSetting.getPosition(),
        entity.getCreationTime(),
        entity.getLastChangeTime(),
        entity.getRemovedAt()
    );
  }

  @Deprecated
  @Override
  public List<RingDto> toDtos(Collection<Ring> entities) {
    return entities.stream().map(this::toDto).toList();
  }

  public List<RingDto> toDtosByDate(Collection<Ring> entities, Instant date) {
    return entities.stream().map(ring -> toDtoByDate(ring, date)).toList();
  }

  @Deprecated
  @Override
  public List<Ring> toEntities(Collection<RingDto> dtos) {
    return dtos.stream().map(this::toEntity).toList();
  }

  public List<Ring> toEntities(Collection<RingDto> dtos, Long radarId) {
    return dtos.stream().map(dto -> toEntity(dto, radarId)).toList();
  }
  @Override
  public Ring toUpdate(Ring target, Ring source) {
    if (source.getCreationTime() != null) {
      target.setCreationTime(source.getCreationTime());
    }
    if (source.getLastChangeTime() != null) {
      target.setLastChangeTime(source.getLastChangeTime());
    } else {
      target.setLastChangeTime(Instant.now());
    }
    if (source.getRemovedAt() != null) {
      target.setRemovedAt(source.getRemovedAt());
    }
    if (source.getRadar() != null) {
      target.setRadar(source.getRadar());
    }
    return target;
  }
}
