package ru.hh.techradar.mapper;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Component;
import ru.hh.techradar.dto.RingDto;
import ru.hh.techradar.entity.Ring;
import ru.hh.techradar.entity.RingSetting;
import ru.hh.techradar.util.Pair;

@Component
public class RingMapperUnited implements BaseMapper<Pair<Ring, RingSetting>, RingDto> {

  @Override
  public Pair<Ring, RingSetting> toEntity(RingDto dto) {
    Instant currentTime = Instant.now();
    var ring = new Ring(
        dto.getId(),
        null,
        currentTime,
        currentTime,
        null
    );
    RingSetting ringSetting = new RingSetting(
        dto.getName(),
        dto.getPosition(),
        ring,
        currentTime
    );
    ring.getSettings().add(ringSetting);
    return new Pair<>(ring, ringSetting);
  }

  @Override
  public RingDto toDto(Pair<Ring, RingSetting> entity) {
    var ring = entity.getFirst();
    var ringSetting = entity.getSecond();
    return new RingDto(
        ring.getId(),
        ringSetting.getName(),
        ringSetting.getPosition()
    );
  }

  @Override
  public List<RingDto> toDtos(Collection<Pair<Ring, RingSetting>> entities) {
    return entities.stream().map(this::toDto).toList();
  }

  @Override
  public List<Pair<Ring, RingSetting>> toEntities(Collection<RingDto> dtos) {
    return dtos.stream().map(this::toEntity).toList();
  }

  @Override
  public Pair<Ring, RingSetting> toUpdate(Pair<Ring, RingSetting> target, Pair<Ring, RingSetting> source) {
    var currentTime = Instant.now();
    return new Pair<>(
        updateRingByTime(source.getFirst(), target.getFirst(), currentTime),
        updateRingSettingByTime(source.getSecond(), target.getSecond(), currentTime)
    );
  }

  public Ring toUpdate(Ring target, Ring source) {
    return updateRingByTime(source, target, Instant.now());
  }

  public RingSetting toUpdate(RingSetting target, RingSetting source) {
    return updateRingSettingByTime(source, target, Instant.now());
  }

  private Ring updateRingByTime(Ring source, Ring target, Instant time) {
    target.setCreationTime(Objects.requireNonNull(source.getCreationTime()));
    target.setLastChangeTime(
        Objects.requireNonNullElse(source.getLastChangeTime(), time)
    );
    target.setRemovedAt(source.getRemovedAt());
    target.setRadar(Objects.requireNonNull(source.getRadar()));
    return target;
  }

  private RingSetting updateRingSettingByTime(RingSetting source, RingSetting target, Instant time) {
    target.setName(Objects.requireNonNull(source.getName()));
    target.setCreationTime(Objects.requireNonNull(source.getCreationTime()));
    target.setLastChangeTime(
        Objects.requireNonNullElse(source.getLastChangeTime(), time)
    );
    target.setPosition(Objects.requireNonNull(source.getPosition()));
    target.setRing(Objects.requireNonNull(source.getRing()));
    return target;
  }
}
