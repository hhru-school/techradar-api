package ru.hh.techradar.controller;

import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.stereotype.Controller;
import ru.hh.techradar.dto.RadarVersionDto;
import ru.hh.techradar.entity.RadarVersion;
import ru.hh.techradar.mapper.RadarVersionMapper;
import ru.hh.techradar.service.BlipEventService;
import ru.hh.techradar.service.RadarService;
import ru.hh.techradar.service.RadarVersionService;

@Controller
@Path("/api/radar_versions")
public class RadarVersionController {
  private final RadarVersionService radarVersionService;
  private final RadarVersionMapper radarVersionMapper;
  private final RadarService radarService;
  private final BlipEventService blipEventService;

  public RadarVersionController(RadarVersionService radarVersionService, RadarVersionMapper radarVersionMapper,
      RadarService radarService,
      BlipEventService blipEventService) {
    this.radarVersionService = radarVersionService;
    this.radarVersionMapper = radarVersionMapper;
    this.radarService = radarService;
    this.blipEventService = blipEventService;
  }
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response findAllByFilter(
      @QueryParam("radarId") @NotNull Long radarId
  ) {
    return Response
        .ok(radarVersionMapper.toDtos(radarVersionService.findAllByRadarId(radarId)))
        .build();
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response save(RadarVersionDto dto) {
    RadarVersion radarVersion = radarVersionMapper.toEntity(dto);
    radarVersion.setBlipEvent(blipEventService.findById(dto.getBlipEventId()));
    radarVersion.setRadar(radarService.findById(dto.getRadarId()));
    return Response
        .ok(radarVersionMapper.toDto(radarVersionService.save(radarVersion)))
        .status(Response.Status.CREATED)
        .build();
  }
}
