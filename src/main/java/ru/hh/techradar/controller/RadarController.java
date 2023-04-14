package ru.hh.techradar.controller;

import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServlet;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.InputStream;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import ru.hh.techradar.service.RadarCSVService;

@Path("/api/radars")
public class RadarController extends HttpServlet {

  private final RadarCSVService radarCSVService;

  @Inject
  public RadarController(RadarCSVService radarCSVService) {
    this.radarCSVService = radarCSVService;
  }

  @POST
  @Path("/upload/csv")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  public Response uploadRadar(
      @FormDataParam("file") InputStream inputStream,
      @FormDataParam("file") FormDataContentDisposition fileDisposition
  ) {
    radarCSVService.uploadRadar(inputStream, fileDisposition);
    return Response
        .status(Response.Status.OK)
        .build();
  }
}
