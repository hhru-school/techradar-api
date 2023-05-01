package ru.hh.techradar.controller;

import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.time.Instant;
import org.springframework.stereotype.Controller;
import ru.hh.techradar.mapper.RadarMapper;
import ru.hh.techradar.service.RadarService;

@Controller
@Path("/api/radars")
public class RadarController {
  private final RadarService radarService;
  private final RadarMapper radarMapper;

  public RadarController(RadarService radarService, RadarMapper radarMapper) {
    this.radarService = radarService;
    this.radarMapper = radarMapper;
  }

  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response findByIdAndFilter(
      @PathParam("id") Long id,
      @QueryParam("actualDate") Instant actualDate,
      @QueryParam("blipEventId") Long blipEventId) {
    return Response
        .ok(radarMapper.toDto(radarService.findById(id)))
        .build();
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response findAllByFilter(
      @QueryParam("companyId") @NotNull Long companyId,
      @QueryParam("actualDate") Instant actualDate
  ) {
    return Response
        .ok(radarMapper.toShortDtos(radarService.findAllByFilter(companyId, actualDate)))
        .build();
  }
}
