package ru.hh.techradar.service;

import com.opencsv.bean.CsvToBeanBuilder;
import jakarta.servlet.http.Part;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import ru.hh.techradar.entity.Blip;
import ru.hh.techradar.entity.BlipEvent;
import ru.hh.techradar.entity.Company;
import ru.hh.techradar.entity.Quadrant;
import ru.hh.techradar.entity.QuadrantSetting;
import ru.hh.techradar.entity.Radar;
import ru.hh.techradar.entity.Ring;
import ru.hh.techradar.entity.RingSetting;
import ru.hh.techradar.entity.User;
import ru.hh.techradar.model.RadarCSV;

@Service
public class RadarCSVService {

  private final UserService userService;
  private final CompanyService companyService;
  private final RadarService radarService;
  private final RingService ringService;
  private final RingSettingsService ringSettingsService;
  private final QuadrantService quadrantService;
  private final QuadrantSettingsService quadrantSettingsService;
  private final BlipService blipService;
  private final BlipEventService blipEventService;

  private final Map<String, Integer> namePosition = new HashMap<>(){{
    //backend
    put("adopt", 1);
    put("trial", 2);
    put("assess", 3);
    put("hold", 4);
    put("(removed)", 5);
    put("remove", 5);
    put("Data Management", 1);
    put("Frameworks & Tools", 2);
    put("Languages", 3);
    put("Platform & Insfrastructure", 4);
    //android
    put("Используется", 1);
    put("Эксперимент", 2);
    put("Устарело", 3);
    put("Оценка", 4);
    put("Подход", 1);
    put("Библиотека", 2);
    put("Сервис", 3);
    put("Фреймворк, Язык", 4);
    put("Языки и тулинг", 4);
    //frontend
    put("Languages & Frameworks", 1);
    put("Tools & Environment", 2);
    put("Techniques", 3);
    put("Libraries", 4);
    //ios
    put("Языки и инструменты", 1);

  }};


  public RadarCSVService(
      UserService userService,
      CompanyService companyService,
      RadarService radarService,
      RingService ringService,
      RingSettingsService ringSettingsService,
      QuadrantService quadrantService,
      QuadrantSettingsService quadrantSettingsService,
      BlipService blipService,
      BlipEventService blipEventService
  ) {
    this.userService = userService;
    this.companyService = companyService;
    this.radarService = radarService;
    this.ringService = ringService;
    this.ringSettingsService = ringSettingsService;
    this.quadrantService = quadrantService;
    this.quadrantSettingsService = quadrantSettingsService;
    this.blipService = blipService;
    this.blipEventService = blipEventService;
  }

  public void uploadRadar(Part file) {
    //todo
//    Company company = companyService.save(new Company("HeadHunter"));
//    User user = userService.save(new User("hh.user", "password", company));
    Company company = companyService.findById(1L);
    User user = userService.findById(1L);
    Map<String, RingSetting> ringSettingMap = new HashMap<>();
    Map<String, QuadrantSetting> quadrantSettingMap = new HashMap<>();
    System.out.println(file.getSubmittedFileName());
    Radar radar = radarService.save(new Radar(file.getSubmittedFileName(), company, user));
    try (
        BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
    ) {
      List<RadarCSV> radarCSVS = new CsvToBeanBuilder(reader)
          .withType(RadarCSV.class)
          .build()
          .parse();
      for (RadarCSV radarCSV : radarCSVS) {
        if ("".equals(radarCSV.getRingName())) break;
        if (!ringSettingMap.containsKey(radarCSV.getRingName())) {
          Ring ring = ringService.save(new Ring(radar));
          RingSetting ringSetting = ringSettingsService.save(new RingSetting(radarCSV.getRingName(), namePosition.get(radarCSV.getRingName()), ring));
          ringSettingMap.put(radarCSV.getRingName(), ringSetting);
        }
        if (!quadrantSettingMap.containsKey(radarCSV.getQuadrantName())) {
          Quadrant quadrant = quadrantService.save(new Quadrant(radar));
          QuadrantSetting quadrantSetting = quadrantSettingsService.save(new QuadrantSetting(radarCSV.getQuadrantName(), namePosition.get(radarCSV.getQuadrantName()), quadrant));
          quadrantSettingMap.put(radarCSV.getQuadrantName(), quadrantSetting);
        }
        Blip blip = blipService.save(new Blip(radarCSV.getBlipName(), radarCSV.getDescription(), radar));
        BlipEvent blipEvent = blipEventService.save(new BlipEvent(null,
            file.getSubmittedFileName().split("-")[1].split("\\.")[0],
            blip,
            quadrantSettingMap.get(radarCSV.getQuadrantName()).getQuadrant(),
            ringSettingMap.get(radarCSV.getRingName()).getRing(),
            user
            )
        );

      }
    } catch (IOException exception) {
      exception.printStackTrace();
      throw new IllegalArgumentException(exception.getMessage());
    }
  }

}
