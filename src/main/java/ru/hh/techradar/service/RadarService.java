package ru.hh.techradar.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hh.techradar.dto.RadarDto;
import ru.hh.techradar.entity.Radar;
import ru.hh.techradar.exception.EntityExistsException;
import ru.hh.techradar.exception.NotFoundException;
import ru.hh.techradar.mapper.RadarMapper;
import ru.hh.techradar.repository.RadarRepository;

@Service
public class RadarService {
  private final RadarRepository radarRepository;
  private final CompanyService companyService;
  private final RadarMapper radarMapper;
  private final UserService userService;


  public RadarService(
      RadarRepository radarRepository,
      CompanyService companyService,
      RadarMapper radarMapper,
      UserService userService
      ) {
    this.radarRepository = radarRepository;
    this.companyService = companyService;
    this.radarMapper = radarMapper;
    this.userService = userService;
  }

  @Transactional
  public Radar save(RadarDto dto) {
    if(radarRepository.findByNameAndCompanyId(dto.getName(), dto.getCompanyId()).isPresent()) {
      throw new EntityExistsException(Radar.class, dto.getName());
    }
    Radar radar = radarMapper.toEntity(dto);
    radar.setAuthor(userService.findById(dto.getAuthorId()));
    radar.setCompany(companyService.findById(dto.getCompanyId()));
    return radarRepository.save(radar);
  }

  @Transactional(readOnly = true)
  public Radar findById(Long id) {
    return radarRepository.findById(id).orElseThrow(() -> new NotFoundException(Radar.class, id));
  }

  @Transactional(readOnly = true)
  public List<Radar> findAllByCompanyId(Long companyId) {
    return radarRepository.findAllByCompanyId(companyId);
  }

  @Transactional
  public Radar update(Long id, Radar radar) {
    Radar found = radarRepository.findById(id).orElseThrow(() -> new NotFoundException(Radar.class, id));
    if(radar.getName() != null && !found.getName().equals(radar.getName()) && radarRepository.findByNameAndCompanyId(radar.getName(), found.getCompany().getId()).isPresent()) {
      throw new EntityExistsException(Radar.class, radar.getName());
    }
    return radarRepository.update(radarMapper.toUpdate(found, radar));
  }

  @Transactional
  public void deleteById(Long id) {
    radarRepository.deleteById(id);
  }
}
