package ru.hh.techradar.controller;

import jakarta.inject.Inject;
import jakarta.servlet.MultipartConfigElement;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.IOException;
import org.eclipse.jetty.server.Request;
import ru.hh.techradar.service.RadarCSVService;

@Path("/api/csv")
public class RadarCSVController extends HttpServlet {

  private final RadarCSVService radarCSVService;

  @Inject
  public RadarCSVController(RadarCSVService radarCSVService) {
    this.radarCSVService = radarCSVService;
  }

  @POST
  @Path("/upload")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  public Response uploadRadar(@Context HttpServletRequest request) throws ServletException, IOException {
    request.setAttribute(Request.__MULTIPART_CONFIG_ELEMENT, new MultipartConfigElement(""));
    radarCSVService.uploadRadar(request.getPart("file"));
    return Response
        .status(Response.Status.OK)
        .build();
  }
}
