package ru.hh.techradar.controller;

import jakarta.ws.rs.Consumes;
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
import ru.hh.techradar.dto.BlipEventDto;
import ru.hh.techradar.mapper.BlipEventMapper;
import ru.hh.techradar.service.BlipEventService;

@Controller
@Path("/api/blip-events")
public class BlipEventController {
  private final BlipEventMapper blipEventMapper;
  private final BlipEventService blipEventService;

  public BlipEventController(BlipEventMapper blipEventMapper, BlipEventService blipEventService) {
    this.blipEventMapper = blipEventMapper;
    this.blipEventService = blipEventService;
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response save(
      @QueryParam("insert") Boolean insert,
      BlipEventDto dto
  ) {
    return Response.ok(blipEventMapper.toDto(blipEventService.save(dto, insert)))
        .status(Response.Status.CREATED)
        .build();
  }

  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response findById(@PathParam("id") Long id) {
    return Response
        .ok(blipEventMapper.toDto(blipEventService.findById(id)))
        .build();
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response find(
      @QueryParam("blip-event-id") Long blipEventId,//TODO: check if using filters is a good idea
      @QueryParam("blip-id") Long blipId
  ) {
    return Response.ok(blipEventMapper.toDtos(blipEventService.find(blipEventId, blipId)))
        .build();
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/radar-log")
  public Response findAllBlipsByBlipEventId(@QueryParam("blip-event-id") Long blipEventId) {
    return Response.ok(blipEventMapper.toDtos(blipEventService.findAllBlipsByBlipEventId(blipEventId)))
        .build();
  }

  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{id}")
  public Response update(
      @PathParam("id") Long blipEventId,
      @QueryParam("move") Boolean move,
      BlipEventDto dto
  ) {
    dto.setId(blipEventId);
    return Response.ok(blipEventMapper.toDto(blipEventService.update(dto, move)))
        .build();
  }
  //TODO: think of how to delete BEs:
  // 1. Isolated.
  // 2. With children and their children and so on.
  // 3. Rebuilding children to brothers first.
}
