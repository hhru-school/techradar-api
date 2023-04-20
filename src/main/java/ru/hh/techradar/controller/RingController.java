package ru.hh.techradar.controller;

import jakarta.inject.Inject;
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
import java.time.Instant;
import ru.hh.techradar.dto.RingDto;
import ru.hh.techradar.mapper.RingMapper;
import ru.hh.techradar.service.RingService;

@Path("/api/rings")
public class RingController {
  private final RingService ringService;
  private final RingMapper ringMapper;

  @Inject
  public RingController(
      RingService ringService,
      RingMapper ringMapper
  ) {
    this.ringService = ringService;
    this.ringMapper = ringMapper;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response findAllByFilter(
      @QueryParam("radarId") Long radarId,
      @QueryParam("actualDate") Instant actualDate
  ) {
    return Response
        .ok(ringMapper.toDtos(ringService.findAllByFilter(radarId, actualDate)))
        .build();
  }

  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response findById(@PathParam("id") Long id) {
    return Response
        .ok(ringMapper.toDto(ringService.findById(id)))
        .build();
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response save(@QueryParam("radarId") Long radarId, RingDto dto) {
    return Response
        .ok(ringMapper.toDto(ringService.save(radarId, ringMapper.toEntity(dto))))
        .status(Response.Status.CREATED)
        .build();
  }

  @PUT
  @Path("/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response update(@PathParam("id") Long id, RingDto dto) {
    return Response
        .ok(ringMapper.toDto(ringService.update(id, ringMapper.toEntity(dto))))
        .build();
  }

  @GET
  @Path("/archive/{id}")
  public Response archiveById(@PathParam("id") Long id) {
    ringService.archiveById(id);
    return Response
        .status(Response.Status.NO_CONTENT)
        .build();
  }
}
