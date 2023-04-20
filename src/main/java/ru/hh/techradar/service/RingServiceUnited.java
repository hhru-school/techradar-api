package ru.hh.techradar.service;

import jakarta.inject.Inject;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hh.techradar.entity.Ring;
import ru.hh.techradar.entity.RingSetting;
import ru.hh.techradar.mapper.RingMapperUnited;
import ru.hh.techradar.repository.RingRepositoryUnited;
import ru.hh.techradar.util.Pair;

@Service
public class RingServiceUnited {
  private final RingSettingService ringSettingService;
  private final RingRepositoryUnited ringRepositoryUnited;
  private final RingService ringService;
  private final RingMapperUnited ringMapperUnited;

  @Inject
  public RingServiceUnited(
      RingSettingService ringSettingService,
      RingRepositoryUnited ringRepositoryUnited, RingService ringService,
      RingMapperUnited ringMapperUnited
  ) {
    this.ringSettingService = ringSettingService;
    this.ringRepositoryUnited = ringRepositoryUnited;
    this.ringService = ringService;
    this.ringMapperUnited = ringMapperUnited;
  }

  @Transactional
  public Pair<Ring, RingSetting> forceUpdate(Long ringId, Pair<Ring, RingSetting> entity) {
    var found = new Pair<>(
        ringService.findById(ringId),
        ringSettingService.findRingSettingByDate(ringId, Instant.now())
    );
    return ringRepositoryUnited.update(ringMapperUnited.toUpdate(found, entity));
  }

  @Transactional
  public Pair<Ring, RingSetting> update(Long ringId, Pair<Ring, RingSetting> entity) {
    return new Pair<>(
        ringService.update(ringId, ringMapperUnited.toUpdate(ringService.findById(ringId), entity.getFirst())),
        ringSettingService.save(entity.getSecond())
    );
  }

  @Transactional(readOnly = true)
  public List<Pair<Ring, RingSetting>> fetchPairsByRadarIdAndDate(Long radarId, Instant actualDate) {
    return ringRepositoryUnited.fetchPairsByRadarIdAndDate(radarId, actualDate);
  }

  @Transactional
  public Pair<Ring, RingSetting> save(Pair<Ring, RingSetting> pair) {
    return new Pair<>(
        ringService.save(pair.getFirst()),
        ringSettingService.save(pair.getSecond())
    );
  }
}
