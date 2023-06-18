package ru.hh.techradar.controller;

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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import ru.hh.techradar.dto.BlipTemplateDto;
import ru.hh.techradar.mapper.BlipTemplateMapper;
import ru.hh.techradar.service.BlipTemplateService;

@Controller
@Path("/api/blip-templates")
public class BlipTemplateController {
  private final BlipTemplateService blipTemplateService;
  private final BlipTemplateMapper blipTemplateMapper;

  public BlipTemplateController(BlipTemplateService blipTemplateService, BlipTemplateMapper blipTemplateMapper) {
    this.blipTemplateService = blipTemplateService;
    this.blipTemplateMapper = blipTemplateMapper;
  }

  @GET
  @Path("/search")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getAutocompleteList(@QueryParam("q") String namePart) {
    return Response.ok(
        blipTemplateMapper.toDtos(blipTemplateService.findByNamePart(namePart))
    ).build();
  }

  @GET
  @Path("/{name}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getByName(@PathParam("name") String name) {
    return Response.ok(
        blipTemplateMapper.toDto(blipTemplateService.findById(name))
    ).build();
  }

  @PreAuthorize("hasAnyAuthority('ADMIN', 'MEMBER')")
  @PUT
  @Path("/{name}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response update(@PathParam("name") String blipToUpdate, BlipTemplateDto blipTemplateDto) {
    return Response.ok(
        blipTemplateMapper.toDto(
            blipTemplateService.update(blipToUpdate, blipTemplateMapper.toEntity(blipTemplateDto))
        )
    ).build();
  }

  @PreAuthorize("hasAnyAuthority('ADMIN', 'MEMBER')")
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response add(BlipTemplateDto blipTemplateDto) {
    return Response.ok(
        blipTemplateMapper.toDto(
            blipTemplateService.save(blipTemplateMapper.toEntity(blipTemplateDto))
        )
    ).build();
  }

  @PreAuthorize("hasAnyAuthority('ADMIN', 'MEMBER')")
  @DELETE
  @Path("/{name}")
  public Response remove(@PathParam("name") String name) {
    blipTemplateService.deleteById(name);
    return Response.status(Response.Status.NO_CONTENT).build();
  }
}
