package ru.hh.techradar.controller;

import jakarta.inject.Inject;
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
import java.time.Instant;
import ru.hh.techradar.dto.QuadrantDto;
import ru.hh.techradar.mapper.QuadrantMapper;
import ru.hh.techradar.service.QuadrantService;

@Path("/api/quadrants")
public class QuadrantController {
  private final QuadrantService quadrantService;
  private final QuadrantMapper quadrantMapper;

  @Inject
  public QuadrantController(
      QuadrantService quadrantService,
      QuadrantMapper quadrantMapper) {
    this.quadrantService = quadrantService;
    this.quadrantMapper = quadrantMapper;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response findAllByFilter(
      @QueryParam("radarId") Long radarId,
      @QueryParam("actualDate") Instant actualDate
  ) {
    return Response
        .ok(quadrantMapper.toDtos(quadrantService.findAllByFilter(radarId, actualDate)))
        .build();
  }

  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response findById(@PathParam("id") Long id) {
    return Response
        .ok(quadrantMapper.toDto(quadrantService.findById(id)))
        .build();
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response save(@QueryParam("radarId") Long radarId, QuadrantDto dto) {
    return Response
        .ok(quadrantMapper.toDto(quadrantService.save(radarId, quadrantMapper.toEntity(dto))))
        .status(Response.Status.CREATED)
        .build();
  }

  @PUT
  @Path("/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response update(@PathParam("id") Long id, QuadrantDto dto) {
    return Response
        .ok(quadrantMapper.toDto(quadrantService.update(id, quadrantMapper.toEntity(dto))))
        .build();
  }

  @GET
  @Path("/archive/{id}")
  public Response archiveById(@PathParam("id") Long id) {
    quadrantService.archiveById(id);
    return Response
        .status(Response.Status.NO_CONTENT)
        .build();
  }
}