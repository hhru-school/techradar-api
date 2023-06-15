package ru.hh.techradar.service;

import com.opencsv.bean.CsvToBeanBuilder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.dhatim.fastexcel.reader.ReadableWorkbook;
import org.dhatim.fastexcel.reader.Row;
import org.dhatim.fastexcel.reader.Sheet;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hh.techradar.dto.BlipCreateDto;
import ru.hh.techradar.dto.BlipDto;
import ru.hh.techradar.dto.BlipEventDto;
import ru.hh.techradar.dto.ContainerCreateDto;
import ru.hh.techradar.dto.QuadrantDto;
import ru.hh.techradar.dto.RadarDto;
import ru.hh.techradar.dto.RingDto;
import ru.hh.techradar.entity.Blip;
import ru.hh.techradar.entity.BlipEvent;
import ru.hh.techradar.entity.Container;
import ru.hh.techradar.entity.Quadrant;
import ru.hh.techradar.entity.RadarVersion;
import ru.hh.techradar.entity.Ring;
import ru.hh.techradar.entity.User;
import ru.hh.techradar.exception.FileException;
import ru.hh.techradar.exception.UniqueException;
import ru.hh.techradar.model.RadarItem;

@Service
public class RadarFileService {

  private final String FILE_EXTENSION_FILE_CSV = ".csv";
  private final String FILE_EXTENSION_FILE_XLSX = ".xlsx";
  private final String EXCEPTION_MESSAGE_EXTENSION_FILE = "Invalid file format!";
  private final String EXCEPTION_MESSAGE_FILE_EMPTY = "File is empty!";
  public static final String DEFAULT_RADAR_VERSION = "V-1";
  private final UserService userService;
  private final ContainerService containerService;
  private final RadarVersionService radarVersionService;
  private final BlipService blipService;
  private final BlipEventService blipEventService;

  public RadarFileService(
      UserService userService,
      ContainerService containerService,
      RadarVersionService radarVersionService,
      BlipService blipService,
      BlipEventService blipEventService) {
    this.userService = userService;
    this.containerService = containerService;
    this.radarVersionService = radarVersionService;
    this.blipService = blipService;
    this.blipEventService = blipEventService;
  }

  @Transactional
  public Map uploadRadar(FormDataMultiPart multipart, String username, Long companyId) {
    FormDataBodyPart bodyPart = multipart.getField("file");
    String filename = new String(bodyPart.getContentDisposition().getParameters().get("filename").getBytes(StandardCharsets.ISO_8859_1));
    InputStream inputStream = bodyPart.getValueAs(InputStream.class);
    if (filename.endsWith(FILE_EXTENSION_FILE_CSV)) {
      return createRadarFromCSV(inputStream, filename, username, companyId);
    } else if (filename.endsWith(FILE_EXTENSION_FILE_XLSX)) {
      return createRadarFromExcel(inputStream, filename, username, companyId);
    } else {
      throw new FileException(EXCEPTION_MESSAGE_EXTENSION_FILE);
    }
  }

  private Map createRadarFromCSV(InputStream inputStream, String filename, String username, Long companyId) {
    List<RadarItem> radarItems = parseCSV(inputStream);
    validate(radarItems);
    ContainerCreateDto containerDto = createContainer(filename, username, radarItems, companyId);
    Container container = containerService.save(containerDto, username);
    radarVersionService.saveReleaseVersion(container, DEFAULT_RADAR_VERSION);
    return Map.of("radarId", container.getRadar().getId());
  }

  private String getRadarName(String filename) {
    if (filename.endsWith(FILE_EXTENSION_FILE_CSV)) {
      filename = filename.replace(FILE_EXTENSION_FILE_CSV, "");
    } else if (filename.endsWith(FILE_EXTENSION_FILE_XLSX)) {
      filename = filename.replace(FILE_EXTENSION_FILE_XLSX, "");
    }
    return filename;
  }

  private Map createRadarFromExcel(InputStream inputStream, String filename, String username, Long companyId) {
    LinkedHashMap<String, List<RadarItem>> sheetNameToRadarFiles = parseExcel(inputStream);
    validate(sheetNameToRadarFiles);
    Container container = null;
    Map<String, RadarItem> blipNameToRadarFile = null;
    Map<String, Quadrant> nameToQuadrant = null;
    Map<String, Ring> nameToRing = null;
    for (var map : sheetNameToRadarFiles.entrySet()) {
      if (!map.getValue().isEmpty()) {
        if (Objects.isNull(container)) {
          container = saveContainer(filename, username, companyId, map);
          nameToQuadrant = container.getQuadrants().stream().collect(Collectors.toMap(Quadrant::getName, Function.identity()));
          nameToRing = container.getRings().stream().collect(Collectors.toMap(Ring::getName, Function.identity()));
          blipNameToRadarFile = map.getValue().stream()
              .filter(r -> Objects.nonNull(r.getBlipName()) && !r.getRingName().isEmpty())
              .collect(Collectors.toMap(RadarItem::getBlipName, Function.identity()));
        } else {
          createAndSaveRadarVersion(username, container, blipNameToRadarFile, nameToQuadrant, nameToRing, map);
        }
      }
    }
    return Map.of("radarId", container.getRadar().getId());
  }

  private Container saveContainer(String filename, String username, Long companyId, Map.Entry<String, List<RadarItem>> map) {
    Container container;
    ContainerCreateDto containerDto = createContainer(filename, username, map.getValue(), companyId);
    container = containerService.save(containerDto, username);
    RadarVersion radarVersion = radarVersionService.saveReleaseVersion(container, map.getKey());
    container.setRadarVersion(radarVersion);
    return container;
  }

  private void createAndSaveRadarVersion(
      String username,
      Container container,
      Map<String, RadarItem> blipNameToRadarFile,
      Map<String, Quadrant> nameToQuadrant,
      Map<String, Ring> nameToRing,
      Map.Entry<String, List<RadarItem>> map) {
    for (RadarItem tempRadar : map.getValue()) {
      if (blipNameToRadarFile.containsKey(tempRadar.getBlipName())) {
        RadarItem saved = blipNameToRadarFile.get(tempRadar.getBlipName());
        if (!saved.getQuadrantName().equals(tempRadar.getQuadrantName()) || !saved.getRingName().equals(tempRadar.getRingName())) {
          BlipEvent blipEvent = blipEventService.save(username, toBlipEventUpdateDto(tempRadar, container, nameToQuadrant, nameToRing), false);
          container.setBlipEvent(blipEvent);
        }
      } else {
        if (nameToQuadrant.containsKey(tempRadar.getQuadrantName()) && nameToRing.containsKey(tempRadar.getRingName())) {
          Blip blip = blipService.save(toBlipDto(tempRadar, container));
          BlipEvent blipEvent = blipEventService.save(username, toBlipEventDto(tempRadar, container, blip), false);
          container.getBlips().add(blip);
          container.setBlipEvent(blipEvent);
          blipNameToRadarFile.put(tempRadar.getBlipName(), tempRadar);
        }
      }
    }
    RadarVersion radarVersion = radarVersionService.saveReleaseVersion(container, map.getKey());
    container.setRadarVersion(radarVersion);
  }

  private List<RadarItem> parseCSV(InputStream inputStream) {
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
      List<RadarItem> radarItems = new CsvToBeanBuilder(reader).withType(RadarItem.class).build().parse();
      if (radarItems.isEmpty()) {
        throw new FileException(EXCEPTION_MESSAGE_FILE_EMPTY);
      }
      return radarItems;
    } catch (IOException exception) {
      throw new FileException(exception.getMessage());
    }
  }

  private LinkedHashMap<String, List<RadarItem>> parseExcel(InputStream inputStream) {
    try (ReadableWorkbook workbook = new ReadableWorkbook(inputStream)) {
      List<Sheet> sheets = workbook.getSheets().collect(Collectors.toCollection(ArrayList::new));
      Collections.reverse(sheets);
      return sheets.stream()
          .collect(
              Collectors.toMap(
                  Sheet::getName,
                  this::toRadarFiles,
                  (rf1, rf2) -> rf1,
                  LinkedHashMap::new)
          );
    } catch (IOException exception) {
      throw new FileException(exception.getMessage());
    }
  }

  private List<RadarItem> toRadarFiles(Sheet sheet) {
    try (Stream<Row> rows = sheet.openStream()) {
      return rows
          .skip(1)
          .filter(r -> Objects.nonNull(r.getCellText(0)) && !r.getCellText(0).isEmpty())
          .map(this::toRadarFile)
          .collect(Collectors.toMap(
              RadarItem::getBlipName,
              Function.identity(),
              (key1, key2) -> key1)
          )
          .values().stream().toList();
    } catch (IOException exception) {
      throw new FileException(exception.getMessage());
    }
  }

  private RadarItem toRadarFile(Row row) {
    RadarItem radarItem = new RadarItem();
    radarItem.setBlipName(row.getCellText(0).trim());
    radarItem.setRingName(row.getCellText(1).trim());
    radarItem.setQuadrantName(row.getCellText(2).trim());
    radarItem.setDescription(row.getCellText(4).trim());
    return radarItem;
  }

  private ContainerCreateDto createContainer(String filename, String username, Collection<RadarItem> radarsFile, Long companyId) {
    ContainerCreateDto containerCreateDto = new ContainerCreateDto();
    containerCreateDto.setBlips(radarsFile.stream().filter(this::isNotEmptyBlipName).map(this::toBlipCreateDto).toList());
    addQuadrantDtoAndRingDto(radarsFile, containerCreateDto);
    containerCreateDto.setRadar(createRadarDto(getRadarName(filename), username, companyId));
    return containerCreateDto;
  }

  private ContainerCreateDto addQuadrantDtoAndRingDto(Collection<RadarItem> radarItems, ContainerCreateDto containerCreateDto) {
    Map<String, QuadrantDto> nameToQuadrantDto = new HashMap<>();
    Map<String, RingDto> nameToRingDto = new HashMap<>();
    for (RadarItem radarItem : radarItems) {
      if (isNotEmptyBlipName(radarItem)) {
        if (!nameToQuadrantDto.containsKey(radarItem.getQuadrantName())) {
          radarItem.setQuadrantPosition(nameToQuadrantDto.size() + 1);
          nameToQuadrantDto.put(radarItem.getQuadrantName(), toQuadrantDto(radarItem));
        }
        if (!nameToRingDto.containsKey(radarItem.getRingName())) {
          radarItem.setRingPosition(nameToRingDto.size() + 1);
          nameToRingDto.put(radarItem.getRingName(), toRingDto(radarItem));
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
    radarDto.setName(radarName);
    radarDto.setAuthorId(user.getId());
    radarDto.setCompanyId(companyId);
    return radarDto;
  }

  private BlipCreateDto toBlipCreateDto(RadarItem radarItems) {
    BlipCreateDto blipCreateDto = new BlipCreateDto();
    blipCreateDto.setName(radarItems.getBlipName());
    blipCreateDto.setDescription(radarItems.getDescription());
    blipCreateDto.setQuadrant(toQuadrantDto(radarItems));
    blipCreateDto.setRing(toRingDto(radarItems));
    return blipCreateDto;
  }

  private QuadrantDto toQuadrantDto(RadarItem radarItem) {
    QuadrantDto quadrantDto = new QuadrantDto();
    quadrantDto.setName(radarItem.getQuadrantName());
    quadrantDto.setPosition(radarItem.getQuadrantPosition());
    return quadrantDto;
  }

  private RingDto toRingDto(RadarItem radarItem) {
    RingDto ringDto = new RingDto();
    ringDto.setName(radarItem.getRingName());
    ringDto.setPosition(radarItem.getRingPosition());
    return ringDto;
  }

  private BlipDto toBlipDto(RadarItem radarItem, Container container) {
    BlipDto blipDto = new BlipDto();
    blipDto.setName(radarItem.getBlipName());
    blipDto.setDescription(radarItem.getDescription());
    blipDto.setRadarId(container.getRadar().getId());
    return blipDto;
  }

  private BlipEventDto toBlipEventUpdateDto(
      RadarItem radarItem,
      Container container,
      Map<String, Quadrant> nameToQuadrant,
      Map<String, Ring> nameToRing) {
    Blip blip = container.getBlips().stream().filter(b -> b.getName().equals(radarItem.getBlipName())).findFirst().get();
    BlipEventDto blipEventDto = new BlipEventDto();
    blipEventDto.setAuthorId(container.getRadar().getAuthor().getId());
    blipEventDto.setRadarId(container.getRadar().getId());
    blipEventDto.setParentId(container.getBlipEvent().getId());
    blipEventDto.setBlipId(blip.getId());
    if (nameToQuadrant.containsKey(radarItem.getQuadrantName()) && nameToRing.containsKey(radarItem.getRingName())) {
      blipEventDto.setRingId(nameToRing.get(radarItem.getRingName()).getId());
      blipEventDto.setQuadrantId(nameToQuadrant.get(radarItem.getQuadrantName()).getId());
    }
    return blipEventDto;
  }

  private BlipEventDto toBlipEventDto(RadarItem radarItem, Container container, Blip blip) {
    BlipEventDto blipEventDto = new BlipEventDto();
    blipEventDto.setAuthorId(container.getRadar().getAuthor().getId());
    blipEventDto.setRadarId(container.getRadar().getId());
    blipEventDto.setBlipId(blip.getId());
    blipEventDto.setParentId(container.getBlipEvent().getId());
    blipEventDto.setQuadrantId(container.getQuadrants().stream().filter(q -> q.getName().equals(radarItem.getQuadrantName())).map(Quadrant::getId).findFirst().get());
    blipEventDto.setRingId(container.getRings().stream().filter(q -> q.getName().equals(radarItem.getRingName())).map(Ring::getId).findFirst().get());
    return blipEventDto;
  }

  private boolean isNotEmptyBlipName(RadarItem radarItem) {
    return Objects.nonNull(radarItem.getBlipName()) && !radarItem.getBlipName().isEmpty();
  }

  private void validate(Collection<RadarItem> radarsFile) {
    String message = validateData(radarsFile, "");
    if (!message.isEmpty()) {
      throw new FileException(message);
    }
    validateUniqueBlip(radarsFile);
  }

  private void validate(LinkedHashMap<String, List<RadarItem>> sheetNameToRadarFiles) {
    String message = "";
    for (var map : sheetNameToRadarFiles.entrySet()) {
      message += validateData(map.getValue(), map.getKey());
    }
    if (!message.isEmpty()) {
      throw new FileException(message);
    }
  }

  private void validateUniqueBlip(Collection<RadarItem> radarsFile) {
    Set<String> names = radarsFile.stream()
        .filter(this::isNotEmptyBlipName)
        .map(RadarItem::getBlipName).collect(Collectors.toSet());
    if (names.isEmpty()) {
      throw new FileException(EXCEPTION_MESSAGE_FILE_EMPTY);
    }
    names = names.stream()
        .filter(name -> Collections.frequency(radarsFile.stream().map(RadarItem::getBlipName).toList(), name) > 1)
        .collect(Collectors.toSet());
    if (!names.isEmpty()) {
      throw new UniqueException(String.format("File contains not unique technology names: %s", names));
    }
  }

  private String validateData(Collection<RadarItem> radarsFile, String beginMessage) {
    StringBuilder message = new StringBuilder(beginMessage);
    for (RadarItem radarItem : radarsFile) {
      if (isNotEmptyBlipName(radarItem)) {
        if (Objects.isNull(radarItem.getQuadrantName()) || radarItem.getQuadrantName().isEmpty()) {
          message.append(String.format("\nFor technology %s not specified %s name!", radarItem.getBlipName(), "sector"));
        }
        if (Objects.isNull(radarItem.getRingName()) || radarItem.getRingName().isEmpty()) {
          message.append(String.format("\nFor technology %s not specified %s name!", radarItem.getBlipName(), "ring"));
        }
      }
    }
    return beginMessage.equals(message.toString()) ? "" : message.toString();
  }
}
