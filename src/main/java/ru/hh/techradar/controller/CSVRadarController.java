package ru.hh.techradar.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.CookieParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.InputStream;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import ru.hh.techradar.service.CSVRadarService;

@Path("/api/csv-radars")
public class CSVRadarController {

  private final CSVRadarService csvRadarService;

  @Inject
  public CSVRadarController(CSVRadarService csvRadarService) {
    this.csvRadarService = csvRadarService;
  }

  @POST
  @Path("/upload")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Produces(MediaType.APPLICATION_JSON)
  public Response uploadRadar(
      @FormDataParam("file") InputStream inputStream,
      @FormDataParam("file") FormDataContentDisposition fileDisposition,
      @CookieParam("username") String username
  ) {
    return Response
        .ok(csvRadarService.uploadRadar(inputStream, fileDisposition, username))
        .build();
  }
}
