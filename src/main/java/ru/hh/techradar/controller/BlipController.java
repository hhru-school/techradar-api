package ru.hh.techradar.controller;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.stereotype.Controller;
import ru.hh.techradar.mapper.BlipMapper;
import ru.hh.techradar.service.BlipService;

@Controller
@Path("/api/blips")
public class BlipController {
  private final BlipMapper blipMapper;
  private final BlipService blipService;

  public BlipController(BlipMapper blipMapper, BlipService blipService) {
    this.blipMapper = blipMapper;
    this.blipService = blipService;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response findByIdAndFilter(
      @QueryParam("blipId") Long blipId, @QueryParam("blipEventId") Long blipEventId) {
    return Response.ok(blipMapper.toDto(blipService.findByIdAndBlipEventId(blipId, blipEventId))).build();
  }
}
