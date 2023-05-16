package ru.hh.techradar.controller;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.BeanParam;
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
import ru.hh.techradar.dto.RingDto;
import ru.hh.techradar.entity.Radar;
import ru.hh.techradar.filter.ComponentFilter;
import ru.hh.techradar.mapper.RingMapper;
import ru.hh.techradar.service.RadarService;
import ru.hh.techradar.service.RingService;

@Path("/api/rings")
public class RingController {
  private final RingService ringService;
  private final RingMapper ringMapper;
  private final RadarService radarService;

  @Inject
  public RingController(RingService ringService, RingMapper ringMapper, RadarService radarService) {
    this.ringService = ringService;
    this.ringMapper = ringMapper;
    this.radarService = radarService;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response findAllByFilter(
      @BeanParam @Valid ComponentFilter filter
  ) {
    return Response
        .ok(ringMapper.toDtos(ringService.findAllByFilter(filter)))
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
    Radar radar = radarService.findById(radarId);
    return Response
        .ok(ringMapper.toDto(ringService.save(radar, ringMapper.toEntity(dto))))
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

  @PUT
  @Path("/archive/{id}")
  public Response archiveById(@PathParam("id") Long id) {
    return Response
        .status(ringService.archiveById(id) ? Response.Status.OK : Response.Status.BAD_REQUEST)
        .build();
  }

  @DELETE
  @Path("/remove/{id}")
  public Response removeById(@PathParam("id") Long id) {
    return Response
        .status(ringService.removeById(id) ? Response.Status.NO_CONTENT : Response.Status.BAD_REQUEST)
        .build();
  }

  @DELETE
  @Path("/force-remove/{id}")
  public Response forceRemoveById(@PathParam("id") Long id) {
    ringService.forceRemoveById(id);
    return Response
        .status(Response.Status.OK)
        .build();
  }

  @GET
  @Path("/contain-blips/{id}")
  public Response isContainBlipsById(@PathParam("id") Long id) {
    return Response.status(
        ringService.isContainBlipsById(id) ? Response.Status.PRECONDITION_FAILED : Response.Status.NO_CONTENT
    ).build();
  }
}
