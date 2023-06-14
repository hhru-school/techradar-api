package ru.hh.techradar.controller;

import jakarta.validation.constraints.NotNull;
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
import ru.hh.techradar.dto.RadarVersionDto;
import ru.hh.techradar.mapper.RadarVersionMapper;
import ru.hh.techradar.mapper.RadarVersionRecursiveMapper;
import ru.hh.techradar.service.RadarVersionService;

@Controller
@Path("/api/radar-versions")
public class RadarVersionController {
  private final RadarVersionService radarVersionService;
  private final RadarVersionMapper radarVersionMapper;
  private final RadarVersionRecursiveMapper radarVersionRecursiveMapper;

  public RadarVersionController(
      RadarVersionService radarVersionService,
      RadarVersionMapper radarVersionMapper,
      RadarVersionRecursiveMapper radarVersionRecursiveMapper) {
    this.radarVersionService = radarVersionService;
    this.radarVersionMapper = radarVersionMapper;
    this.radarVersionRecursiveMapper = radarVersionRecursiveMapper;
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response save(RadarVersionDto dto, @QueryParam("link-to-last-release") Boolean toLastRelease) {
    return Response
        .ok(radarVersionMapper.toDto(radarVersionService.save(dto, toLastRelease)))
        .status(Response.Status.CREATED)
        .build();
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response findAllByRadarId(
      @QueryParam("radar-id") @NotNull Long radarId
  ) {
    return Response
        .ok(radarVersionMapper.toDtos(radarVersionService.findByRadarId(radarId)))
        .build();
  }

  @GET
  @Path("/released-versions")
  @Produces(MediaType.APPLICATION_JSON)
  public Response findAllReleasedRadarVersionsByRadarId(
      @QueryParam("radar-id") @NotNull Long radarId
  ) {
    return Response
        .ok(radarVersionMapper.toDtos(radarVersionService.findAllReleasedRadarVersions(radarId)))
        .build();
  }

  @GET
  @Path("/last-released-version")
  @Produces(MediaType.APPLICATION_JSON)
  public Response findLatestRadarVersion(
      @QueryParam("radar-id") @NotNull Long radarId
  ) {
    return Response
        .ok(radarVersionMapper.toDto(radarVersionService.findLastReleasedRadarVersion(radarId)))
        .build();
  }

  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response findByIdAndFilter(
      @PathParam("id") Long id
  ) {
    return Response
        .ok(radarVersionMapper.toDto(radarVersionService.findById(id)))
        .build();
  }

  @GET
  @Path("/graph")
  @Produces(MediaType.APPLICATION_JSON)
  public Response findGraph(
      @QueryParam("radar-id") Long radarId
  ) {
    return Response
        .ok(radarVersionRecursiveMapper.toDto(radarVersionService.findRoot(radarId)))
        .build();
  }

  @PUT
  @Path("/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response update(@PathParam("id") Long id, RadarVersionDto dto) {
    return Response
        .ok(radarVersionMapper.toDto(radarVersionService.update(id, dto)))
        .build();
  }

  @DELETE
  @Path("/{id}")
  public Response deleteById(@PathParam("id") Long id) {
    radarVersionService.deleteById(id);
    return Response
        .status(Response.Status.NO_CONTENT)
        .build();
  }
}
