package ru.hh.techradar.controller;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.stereotype.Controller;
import ru.hh.techradar.mapper.BlipEventMapper;
import ru.hh.techradar.service.BlipEventService;

@Controller
@Path("/api/blip_events")
public class BlipEventController {
  private final BlipEventMapper blipEventMapper;
  private final BlipEventService blipEventService;

  public BlipEventController(BlipEventMapper blipEventMapper, BlipEventService blipEventService) {
    this.blipEventMapper = blipEventMapper;
    this.blipEventService = blipEventService;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response findAll() {
    return Response.ok(blipEventMapper.toDtos(blipEventService.findAll()))
        .build();
  }
}
