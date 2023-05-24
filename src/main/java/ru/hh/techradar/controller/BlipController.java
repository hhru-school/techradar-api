package ru.hh.techradar.controller;

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
import ru.hh.techradar.dto.BlipDto;
import ru.hh.techradar.entity.Blip;
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

  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response findById(
      @PathParam("id") Long blipId
  ) {
    return Response.ok(blipMapper.toDto(blipService.findById(blipId)))
        .build();
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response findByFilter(
      @QueryParam("blip-event-id") Long blipEventId
  ) {
    return Response
        .ok(blipMapper.toDtos(blipService.findAllByBlipEventId(blipEventId)))
        .build();
  }


  //TODO: get all blips
  // by radarId (should they have status of current revision?),
  // by companyId (should they have status of current revisions of corresponding radars?),
  // by blipEventId (with coordinates) and
  // by radarVersionId (with coordinates)

  @PUT
  @Path("/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response update(@PathParam("id") Long id, BlipDto dto) {
    return Response
        .ok(blipMapper.toDto(blipService.update(id, blipMapper.toEntity(dto))))
        .build();
  }

  //TODO: looks like it should be cascade delete (DB level) for linked blip events. Should think of it.
  @DELETE
  @Path("/{id}")
  public Response deleteById(@PathParam("id") Long id) {
    blipService.deleteById(id);
    return Response
        .status(Response.Status.NO_CONTENT)
        .build();
  }
}
