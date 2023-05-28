package ru.hh.techradar.controller;

import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
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
@Path("/api/radar-versions")
public class RadarVersionController {
  private final RadarVersionService radarVersionService;
  private final RadarVersionMapper radarVersionMapper;
  private final RadarService radarService;
  private final BlipEventService blipEventService;

  public RadarVersionController(
      RadarVersionService radarVersionService, RadarVersionMapper radarVersionMapper,
      RadarService radarService,
      BlipEventService blipEventService) {
    this.radarVersionService = radarVersionService;
    this.radarVersionMapper = radarVersionMapper;
    this.radarService = radarService;
    this.blipEventService = blipEventService;
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response save(RadarVersionDto dto) {
    //TODO: think of how to down it to service (need a piece of advice).
    // The problem is a cycle dependencies
    RadarVersion radarVersion = radarVersionMapper.toEntity(dto);
    radarVersion.setBlipEvent(blipEventService.findById(dto.getBlipEventId()));
    radarVersion.setRadar(radarService.findById(dto.getRadarId()));
    return Response
        .ok(radarVersionMapper.toDto(radarVersionService.save(radarVersion)))
        .status(Response.Status.CREATED)
        .build();
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response findAllByFilter(
      @QueryParam("radar-id") @NotNull Long radarId,
      @QueryParam("last-released-version") Boolean demandLast
  ) {
    return Response
        .ok(radarVersionMapper.toDtos(radarVersionService.findByRadarId(radarId, demandLast)))
        .build();
  }

  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response findByIdAndFilter(
      @PathParam("id") Long id
  ) {
    return Response
        .ok(radarVersionMapper.toDto(radarVersionService.findById(id)))
        .build();
  }

  @PUT
  @Path("/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response update(@PathParam("id") Long id, RadarVersionDto dto) {
    return Response
        .ok(radarVersionMapper.toDto(radarVersionService.update(id, dto)))
        .build();
  }

  @DELETE
  @Path("/{id}")
  public Response deleteById(@PathParam("id") Long id) {
    radarVersionService.deleteById(id);
    return Response
        .status(Response.Status.NO_CONTENT)
        .build();
  }
}
