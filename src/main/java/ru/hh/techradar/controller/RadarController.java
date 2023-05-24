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
import java.time.Instant;
import org.springframework.stereotype.Controller;
import ru.hh.techradar.dto.RadarDto;
import ru.hh.techradar.mapper.RadarMapper;
import ru.hh.techradar.service.RadarService;

@Controller
@Path("/api/radars")
public class RadarController {
  private final RadarService radarService;
  private final RadarMapper radarMapper;

  public RadarController(RadarService radarService, RadarMapper radarMapper) {
    this.radarService = radarService;
    this.radarMapper = radarMapper;
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response save(RadarDto dto) {
    return Response
        .ok(radarMapper.toDto(radarService.save(dto)))
        .status(Response.Status.CREATED)
        .build();
  }

  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response findById(
      @PathParam("id") Long id
  ) {
    return Response
        .ok(radarMapper.toDto(radarService.findById(id)))
        .build();
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response findAllByFilter(
      @QueryParam("company-id") @NotNull Long companyId,
      @QueryParam("actual-date") Instant actualDate
  ) {
    return Response
        .ok(radarMapper.toShortDtos(radarService.findAllByFilter(companyId, actualDate)))
        .build();
  }

  @PUT
  @Path("/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response update(@PathParam("id") Long id, RadarDto dto) {
    return Response
        .ok(radarMapper.toDto(radarService.update(id, radarMapper.toEntity(dto))))
        .build();
  }

  //TODO: looks like it should be cascade delete (mb on DB level). Or should we actually delete radar? Mb it's better to archive it?
  @DELETE
  @Path("/{id}")
  public Response deleteById(@PathParam("id") Long id) {
    radarService.deleteById(id);
    return Response
        .status(Response.Status.NO_CONTENT)
        .build();
  }
}
