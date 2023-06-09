package ru.hh.techradar.service;

import com.opencsv.bean.CsvToBeanBuilder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.springframework.stereotype.Service;
import ru.hh.techradar.dto.BlipCreateDto;
import ru.hh.techradar.dto.ContainerCreateDto;
import ru.hh.techradar.dto.QuadrantDto;
import ru.hh.techradar.dto.RadarDto;
import ru.hh.techradar.dto.RingDto;
import ru.hh.techradar.entity.Company;
import ru.hh.techradar.entity.User;
import ru.hh.techradar.exception.CSVFormatException;
import ru.hh.techradar.exception.UniqueException;
import ru.hh.techradar.model.RadarCSV;

@Service
public class CSVRadarService {

  private final String FILE_EXTENSION_CSV = "csv";
  private final String EXCEPTION_MESSAGE_FILE_EXTENSION_CSV = "Invalid file format!";
  private final String EXCEPTION_MESSAGE_EMPTY = "File is empty!";
  private final UserService userService;
  private final CompanyService companyService;

  public CSVRadarService(UserService userService, CompanyService companyService) {
    this.userService = userService;
    this.companyService = companyService;
  }

  public ContainerCreateDto uploadRadar(
      InputStream inputStream,
      FormDataContentDisposition fileDisposition,
      String username,
      Long companyId) {
    if (!fileDisposition.getFileName().endsWith(FILE_EXTENSION_CSV)) {
      throw new CSVFormatException(EXCEPTION_MESSAGE_FILE_EXTENSION_CSV);
    }

    ContainerCreateDto containerCreateDto;

    try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
      List<RadarCSV> radarsCSV = new CsvToBeanBuilder(reader)
          .withType(RadarCSV.class)
          .build()
          .parse();
      validate(radarsCSV);
      containerCreateDto = createContainerCreateDto(fileDisposition.getFileName(), username, radarsCSV, companyId);
    } catch (IOException exception) {
      throw new CSVFormatException(exception.getMessage());
    }
    return containerCreateDto;
  }

  private ContainerCreateDto createContainerCreateDto(String radarName, String username, List<RadarCSV> radarsCSV, Long companyId) {
    ContainerCreateDto containerCreateDto = new ContainerCreateDto();
    containerCreateDto.setBlips(radarsCSV.stream().filter(this::isNotEmptyBlipName).map(this::toBlipCreateDto).toList());
    addQuadrantDtoAndRingDto(radarsCSV, containerCreateDto);
    containerCreateDto.setRadar(createRadarDto(radarName, username, companyId));
    return containerCreateDto;
  }

  private ContainerCreateDto addQuadrantDtoAndRingDto(List<RadarCSV> radarsCSV, ContainerCreateDto containerCreateDto) {
    Map<String, QuadrantDto> nameToQuadrantDto = new HashMap<>();
    Map<String, RingDto> nameToRingDto = new HashMap<>();
    for (RadarCSV radarCSV : radarsCSV) {
      if (isNotEmptyBlipName(radarCSV)) {
        if (!nameToQuadrantDto.containsKey(radarCSV.getQuadrantName())) {
          radarCSV.setQuadrantPosition(nameToQuadrantDto.size() + 1);
          nameToQuadrantDto.put(radarCSV.getQuadrantName(), toQuadrantDto(radarCSV));
        }
        if (!nameToRingDto.containsKey(radarCSV.getRingName())) {
          radarCSV.setRingPosition(nameToRingDto.size() + 1);
          nameToRingDto.put(radarCSV.getRingName(), toRingDto(radarCSV));
        }
      }
    }
    containerCreateDto.setQuadrants(nameToQuadrantDto.values().stream().sorted(Comparator.comparing(QuadrantDto::getPosition)).toList());
    containerCreateDto.setRings(nameToRingDto.values().stream().sorted(Comparator.comparing(RingDto::getPosition)).toList());
    return containerCreateDto;
  }

  private RadarDto createRadarDto(String radarName, String username, Long companyId) {
    RadarDto radarDto = new RadarDto();
    User user = userService.findByUsername(username);
    Company company = companyService.findById(companyId);
    radarDto.setName(radarName);
    radarDto.setAuthorId(user.getId());
    radarDto.setCompanyId(company.getId());
    return radarDto;
  }

  private BlipCreateDto toBlipCreateDto(RadarCSV radarsCSV) {
    BlipCreateDto blipCreateDto = new BlipCreateDto();
    blipCreateDto.setName(radarsCSV.getBlipName());
    blipCreateDto.setDescription(radarsCSV.getDescription());
    blipCreateDto.setQuadrant(toQuadrantDto(radarsCSV));
    blipCreateDto.setRing(toRingDto(radarsCSV));
    return blipCreateDto;
  }

  private QuadrantDto toQuadrantDto(RadarCSV radarCSV) {
    QuadrantDto quadrantDto = new QuadrantDto();
    quadrantDto.setName(radarCSV.getQuadrantName());
    quadrantDto.setPosition(radarCSV.getQuadrantPosition());
    return quadrantDto;
  }

  private RingDto toRingDto(RadarCSV radarsCSV) {
    RingDto ringDto = new RingDto();
    ringDto.setName(radarsCSV.getRingName());
    ringDto.setPosition(radarsCSV.getRingPosition());
    return ringDto;
  }

  private boolean isNotEmptyBlipName(RadarCSV radarCSV) {
    return Objects.nonNull(radarCSV.getBlipName()) && !radarCSV.getBlipName().isEmpty();
  }

  private void validate(List<RadarCSV> radarsCSV) {
    Set<String> blipNames = new HashSet<>();
    StringBuilder message = new StringBuilder("");
    for (RadarCSV radarCSV : radarsCSV) {
      String blipName = radarCSV.getBlipName();
      if (isNotEmptyBlipName(radarCSV)) {
        blipNames.add(radarCSV.getBlipName());
        if (Objects.isNull(radarCSV.getQuadrantName()) || radarCSV.getQuadrantName().isEmpty()) {
          message.append(String.format("For technology %s not specified %s name!\n", blipName, "sector"));
        }
        if (Objects.isNull(radarCSV.getRingName()) || radarCSV.getRingName().isEmpty()) {
          message.append(String.format("For technology %s not specified %s name!\n", blipName, "ring"));
        }
      }
    }
    if (blipNames.isEmpty()) {
      throw new CSVFormatException(EXCEPTION_MESSAGE_EMPTY);
    }
    Set<String> names = radarsCSV.stream()
        .filter(this::isNotEmptyBlipName)
        .map(RadarCSV::getBlipName)
        .filter(name -> Collections.frequency(radarsCSV.stream().map(RadarCSV::getBlipName).toList(), name) > 1)
        .collect(Collectors.toSet());
    if (!names.isEmpty()) {
      throw new UniqueException(String.format("File contains not unique technology names: %s", names));
    }
    if (!message.isEmpty()) {
      throw new CSVFormatException(message.toString());
    }
  }
}
