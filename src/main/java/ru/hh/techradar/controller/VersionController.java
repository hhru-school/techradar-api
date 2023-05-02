package ru.hh.techradar.controller;

import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.stereotype.Controller;
import ru.hh.techradar.mapper.RadarMapper;
import ru.hh.techradar.service.RadarService;
import ru.hh.techradar.service.VersionService;
@Controller
@Path("/api/versions")
public class VersionController {
  private final RadarService radarService;
  private final RadarMapper radarMapper;
  private final VersionService versionService;

  public VersionController(RadarService radarService, RadarMapper radarMapper, VersionService versionService) {
    this.radarService = radarService;
    this.radarMapper = radarMapper;
    this.versionService = versionService;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response findAllByFilter(
      @QueryParam("radarId") @NotNull Long radarId
  ) {
    return Response
        .ok(radarMapper.toDtos(versionService.findAllByFilter(radarId)))
        .build();
  }

  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response findByIdAndFilter(
      @PathParam("id") Long id,
      @QueryParam("radarId") Long radarId) {
    return Response
        .ok(versionService.findRevisionOfRadarById(radarId, id))
        .build();
  }



//  @POST
//  @Consumes(MediaType.APPLICATION_JSON)
//  @Produces(MediaType.APPLICATION_JSON)
//  public Response save(RadarCreateDto dto) {
//    return Response
//        .ok(radarMapper.toDto(radarService.save(
//            radarMapper.toEntityFromCreateDto(dto),
//            dto.getAuthorId(),
//            dto.getCompanyId()
//        )))
//        .status(Response.Status.CREATED)
//        .build();
//
//  }
}
