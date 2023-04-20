package ru.hh.techradar.controller;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
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
  public Response findByIdAnd(
      @PathParam("id") Long id,
      @QueryParam("blipEventId") Long blipEventId
  ) {
    return Response
        .ok(radarMapper.toDto(radarService.findByIdAndBlipEventId(id, blipEventId)))
        .build();
  }
}
