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
import java.util.Optional;
import ru.hh.techradar.dto.RingDto;
import ru.hh.techradar.filter.ComponentFilter;
import ru.hh.techradar.filter.DateIdFilter;
import ru.hh.techradar.mapper.RingMapper;
import ru.hh.techradar.service.RingService;

@Path("/api/rings")
public class RingController {
  private final RingService ringService;
  private final RingMapper ringMapper;

  @Inject
  public RingController(RingService ringService, RingMapper ringMapper) {
    this.ringService = ringService;
    this.ringMapper = ringMapper;
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
  public Response save(@QueryParam("radar-id") Long radarId, @Valid RingDto dto) {
    return Response
        .ok(ringMapper.toDto(ringService.save(radarId, ringMapper.toEntity(dto), Optional.empty())))
        .status(Response.Status.CREATED)
        .build();
  }

  @PUT
  @Path("/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response update(@PathParam("id") Long id, @Valid RingDto dto) {
    return Response
        .ok(ringMapper.toDto(ringService.update(id, ringMapper.toEntity(dto), Optional.empty())))
        .build();
  }

  @PUT
  @Path("/archive/{id}")
  public Response archiveByFilter(@Valid @BeanParam DateIdFilter filter) {
    Response.Status responseStatus;
    if (ringService.archiveByFilter(filter)) {
      responseStatus = Response.Status.OK;
    } else {
      responseStatus = Response.Status.BAD_REQUEST;
    }
    return Response.status(responseStatus).build();
  }

  @GET
  @Path("/contain-blips/{id}")
  public Response isContainBlipsByFilter(@Valid @BeanParam DateIdFilter filter) {
    Response.Status responseStatus;
    if (ringService.isContainBlipsByFilter(filter)) {
      responseStatus = Response.Status.PRECONDITION_FAILED;
    } else {
      responseStatus = Response.Status.NO_CONTENT;
    }
    return Response.status(responseStatus).build();
  }
}
