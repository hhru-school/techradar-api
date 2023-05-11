package ru.hh.techradar.controller;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.stereotype.Controller;
import ru.hh.techradar.dto.ContainerCreateDto;
import ru.hh.techradar.mapper.ContainerMapper;
import ru.hh.techradar.service.ContainerService;

@Controller
@Path("/api/containers")
public class ContainerController {
private final ContainerMapper containerMapper;
private final ContainerService containerService;

  public ContainerController(ContainerMapper containerMapper, ContainerService containerService) {
    this.containerMapper = containerMapper;
    this.containerService = containerService;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response findByBlipEventId(
      @QueryParam("blipEventId") Long blipEventId) {
    return Response
        .ok(containerMapper.toDto(containerService.findByBlipEventId(blipEventId)))
        .build();
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response save(ContainerCreateDto dto) {
    return Response
        .ok(containerMapper.toDto(containerService.save(dto)))
        .status(Response.Status.CREATED)
        .build();

  }
}
