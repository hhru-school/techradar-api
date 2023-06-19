package ru.hh.techradar.controller;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import static ru.hh.techradar.controller.UtilService.getUsername;
import ru.hh.techradar.service.RadarFileService;

@Path("/api/file-radars")
@Controller
public class RadarFileController {

  private final RadarFileService radarFileService;

  public RadarFileController(RadarFileService radarFileService) {
    this.radarFileService = radarFileService;
  }

  @PreAuthorize("hasAnyAuthority('ADMIN', 'MEMBER')")
  @POST
  @Path("/upload")
  @Consumes(MediaType.MULTIPART_FORM_DATA + ";charset=UTF-8")
  @Produces(MediaType.APPLICATION_JSON)
  public Response uploadRadar(
      FormDataMultiPart multipart,
      @QueryParam("company-id") Long companyId) {
    return Response
        .ok(radarFileService.uploadRadar(multipart, getUsername(), companyId))
        .status(Response.Status.CREATED)
        .build();
  }
}
