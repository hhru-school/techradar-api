package ru.hh.techradar.controller;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.BeanParam;
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
import ru.hh.techradar.dto.QuadrantDto;
import ru.hh.techradar.filter.ComponentFilter;
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
  public Response findAllByFilter(@Valid @BeanParam ComponentFilter filter) {
    return Response
        .ok(quadrantMapper.toDtos(quadrantService.findAllByFilter(filter)))
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
  public Response archiveById(@PathParam("id") Long id, @Valid @BeanParam ComponentFilter filter) {
    quadrantService.archiveByIdAndFilter(id, filter);
    return Response
        .ok(quadrantService.archiveByIdAndFilter(id, filter))
        .build();
  }

  @GET
  @Path("/contain-blips/{id}")
  public Response isContainBlipsById(@PathParam("id") Long id, @Valid @BeanParam ComponentFilter filter) {
    return Response.status(
        quadrantService.isContainBlipsByIdAndFilter(id, filter) ? Response.Status.PRECONDITION_FAILED : Response.Status.NO_CONTENT
    ).build();
  }
}
