package ru.hh.techradar.controller;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.stereotype.Controller;
import ru.hh.techradar.dto.BlipEventDto;
import ru.hh.techradar.dto.RadarDto;
import ru.hh.techradar.entity.BlipEvent;
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

//  @POST
//  @Consumes(MediaType.APPLICATION_JSON)
//  @Produces(MediaType.APPLICATION_JSON)
//  public Response save(@QueryParam("insert") Boolean insert, BlipEventDto dto) {
//    BlipEvent blipEvent;
//    if (insert) {
//      blipEvent = blipEventService.insert(dto);
//    } else {
//      blipEvent = blipEventService.save(dto);
//    }
//    return Response.ok(blipEventMapper.toDto(blipEvent))
//        .status(Response.Status.CREATED)
//        .build();
//  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response save(BlipEventDto dto) {
    return Response.ok(blipEventMapper.toDto(blipEventService.save(dto)))
        .status(Response.Status.CREATED)
        .build();
  }

  @POST
  @Path("/insert")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response insert(BlipEventDto dto) {
    return Response.ok(blipEventMapper.toDto(blipEventService.insert(dto)))
        .build();
  }

  @POST
  @Path("/move_and_insert")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response moveAndInsert(BlipEventDto dto) {
    return Response.ok(blipEventMapper.toDto(blipEventService.moveAndInsert(dto)))
        .build();
  }
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/radar_log")
  public Response findAllBlipsByBlipEventId(@QueryParam("blipEventId") Long blipEventID) {
    return Response.ok(blipEventMapper.toDtos(blipEventService.findAllBlipsByBlipEventId(blipEventID)))
        .build();
  }
}
