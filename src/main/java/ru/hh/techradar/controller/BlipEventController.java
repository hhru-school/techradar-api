package ru.hh.techradar.controller;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.CookieParam;
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
import static ru.hh.techradar.controller.UtilService.getUsername;
import ru.hh.techradar.dto.BlipEventDto;
import ru.hh.techradar.mapper.BlipEventMapper;
import ru.hh.techradar.mapper.BlipEventReadMapper;
import ru.hh.techradar.service.BlipEventService;

@Controller
@Path("/api/blip-events")
public class BlipEventController {
  private final BlipEventMapper blipEventMapper;
  private final BlipEventService blipEventService;
  private final BlipEventReadMapper blipEventReadMapper;

  public BlipEventController(BlipEventMapper blipEventMapper, BlipEventService blipEventService, BlipEventReadMapper blipEventReadMapper) {
    this.blipEventMapper = blipEventMapper;
    this.blipEventService = blipEventService;
    this.blipEventReadMapper = blipEventReadMapper;
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response save(
      @QueryParam("radar-version-id") Long radarVersionId,
      BlipEventDto dto) {
    return Response.ok(blipEventMapper.toDto(blipEventService.save(getUsername(), dto, radarVersionId)))
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
      @QueryParam("blip-id") Long blipId
  ) {
    return Response.ok(blipEventReadMapper.toDtos(blipEventService.find(blipId)))
        .build();
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/radar-log")
  public Response findAllBlipsByBlipEventId(@QueryParam("blip-event-id") Long blipEventId) {
    return Response.ok(blipEventReadMapper.toDtos(blipEventService.findAllBlipsByBlipEventId(blipEventId)))
        .build();
  }

  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{id}")
  public Response update(
      @PathParam("id") Long blipEventId,
      @QueryParam("is-move") Boolean isMove,
      BlipEventDto dto
  ) {
    dto.setId(blipEventId);
    return Response.ok(blipEventMapper.toDto(blipEventService.update(dto, isMove)))
        .build();
  }

  @DELETE
  @Path("/{id}")
  public Response deleteById(@PathParam("id") Long id) {
    blipEventService.deleteByIdChildrenPromoteToBrothers(id);
    return Response
        .status(Response.Status.NO_CONTENT)
        .build();
  }
}
