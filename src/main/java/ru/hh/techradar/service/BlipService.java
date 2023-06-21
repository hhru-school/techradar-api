package ru.hh.techradar.service;

import jakarta.validation.Valid;
import jakarta.validation.Validator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hh.techradar.dto.BlipDto;
import ru.hh.techradar.entity.Blip;
import ru.hh.techradar.entity.Radar;
import ru.hh.techradar.entity.RadarVersion;
import ru.hh.techradar.exception.NotFoundException;
import ru.hh.techradar.exception.UniqueException;
import ru.hh.techradar.filter.BlipFilter;
import ru.hh.techradar.filter.ComponentFilter;
import ru.hh.techradar.mapper.BlipMapper;
import ru.hh.techradar.repository.BlipRepository;

@Service
public class BlipService {
  public static final String BLIP_COLLECTION_CONTAINS_NOT_UNIQUE_NAMES = "Blip collection contains not unique names: %s";
  private final BlipMapper blipMapper;
  private final BlipRepository blipRepository;
  private final RadarService radarService;
  private final Validator validator;

  public BlipService(
      BlipMapper blipMapper,
      BlipRepository blipRepository, RadarService radarService, Validator validator) {
    this.blipMapper = blipMapper;
    this.blipRepository = blipRepository;
    this.radarService = radarService;
    this.validator = validator;
  }

  @Transactional
  public Blip save(BlipDto dto) {
    Blip blip = blipMapper.toEntity(dto);
    blip.setRadar(radarService.findById(dto.getRadarId()));
    validator.validate(blip);
    return blipRepository.save(blip);
  }

  @Transactional
  public Blip saveEntity(@Valid Blip blip) {
    return blipRepository.save(blip);
  }

  @Transactional
  public List<Blip> saveAll(Collection<Blip> blips, Radar radar) {
    validateUniqueness(blips);
    List<Blip> resultList = new ArrayList<>();
    blips.stream().peek(b -> {
      b.setRadar(radar);
      resultList.add(saveEntity(b));
    }).forEach(validator::validate);

    return resultList;
  }

  private void validateUniqueness(Collection<Blip> blips) {
    Set<String> names = blips.stream()
        .map(Blip::getName)
        .filter(name -> Collections.frequency(blips.stream().map(Blip::getName).toList(), name) > 1)
        .collect(Collectors.toSet());
    if (!names.isEmpty()) {
      throw new UniqueException(String.format(BLIP_COLLECTION_CONTAINS_NOT_UNIQUE_NAMES, names));
    }
  }

  @Transactional(readOnly = true)
  public List<Blip> findAll() {
    return blipRepository.findAll();
  }

  @Transactional(readOnly = true)
  public Blip findByFilter(@Valid BlipFilter filter) {
    return blipRepository.findByFilter(filter);
  }

  @Transactional(readOnly = true)
  public List<Blip> findAllByBlipEventId(Long blipEventId) {
    return blipRepository.findActualBlipsByBlipEventId(blipEventId);
  }

  @Transactional(readOnly = true)
  public List<Blip> findAllByFilter(@Valid ComponentFilter filter) {
    return blipRepository.findActualBlipsByFilter(filter);
  }

  @Transactional(readOnly = true)
  public Blip findById(Long id) {
    return blipRepository.findById(id).orElseThrow(() -> new NotFoundException(Blip.class, id));
  }


  @Transactional
  public Blip update(Long id, Blip entity) {
    Blip found = blipRepository.findById(id).orElseThrow(() -> new NotFoundException(Blip.class, id));
    entity = blipMapper.toUpdate(found, entity);
    validator.validate(entity);
    return blipRepository.update(entity);
  }

  @Transactional
  public void deleteById(Long id) {
    blipRepository.deleteById(id);
  }

  public List<Blip> findActualBlipsByRadarVersionWithDrawInfo(@Valid RadarVersion radarVersion) {
    return blipRepository.findActualBlipsByRadarVersionWithDrawInfo(radarVersion);
  }
}
