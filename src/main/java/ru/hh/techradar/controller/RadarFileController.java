package ru.hh.techradar.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import static ru.hh.techradar.controller.UtilService.getUsername;
import ru.hh.techradar.service.RadarFileService;

@Path("/api/file-radars")
public class RadarFileController {

  private final RadarFileService radarFileService;

  @Inject
  public RadarFileController(RadarFileService radarFileService) {
    this.radarFileService = radarFileService;
  }

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
