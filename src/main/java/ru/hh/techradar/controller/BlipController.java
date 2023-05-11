package ru.hh.techradar.controller;

import jakarta.validation.Valid;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.stereotype.Controller;
import ru.hh.techradar.dto.BlipDto;
import ru.hh.techradar.entity.Blip;
import ru.hh.techradar.filter.BlipFilter;
import ru.hh.techradar.mapper.BlipMapper;
import ru.hh.techradar.service.BlipService;
import ru.hh.techradar.service.RadarService;

@Controller
@Path("/api/blips")
public class BlipController {
  private final BlipMapper blipMapper;
  private final BlipService blipService;
  private final RadarService radarService;

  public BlipController(BlipMapper blipMapper, BlipService blipService, RadarService radarService) {
    this.blipMapper = blipMapper;
    this.blipService = blipService;
    this.radarService = radarService;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response findByIdAndFilter(
      @Valid @BeanParam BlipFilter filter
     ) {
    return Response.ok(blipMapper.toDto(blipService.findByIdAndFilter(filter))).build();
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response save(BlipDto dto) {
    Blip blip = blipMapper.toEntity(dto);
    blip.setRadar(radarService.findById(dto.getRadarId()));
    return Response
        .ok(blipMapper.toDto(blipService.save(blip)))
        .status(Response.Status.CREATED)
        .build();
  }
}
