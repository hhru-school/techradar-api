package ru.hh.techradar.controller;

import jakarta.validation.Valid;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import ru.hh.techradar.dto.QuadrantDto;
import ru.hh.techradar.filter.ComponentFilter;
import ru.hh.techradar.mapper.QuadrantMapper;
import ru.hh.techradar.service.QuadrantService;

@Controller
@Path("/api/quadrants")
public class QuadrantController {
  private final QuadrantService quadrantService;
  private final QuadrantMapper quadrantMapper;

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

  @PreAuthorize("hasAnyAuthority('ADMIN', 'MEMBER')")
  @PUT
  @Path("/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response update(@PathParam("id") Long id, @Valid QuadrantDto dto) {
    return Response
        .ok(quadrantMapper.toDto(quadrantService.update(id, quadrantMapper.toEntity(dto))))
        .build();
  }
}
