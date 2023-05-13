package ru.hh.techradar.security.controller;

import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import ru.hh.techradar.security.dto.AuthenticationDto;
import ru.hh.techradar.security.dto.RegisterDto;
import ru.hh.techradar.security.service.AuthenticationService;

@Path("/api/auth")
public class AuthenticationController {

  private final AuthenticationService authenticationService;

  @Inject
  public AuthenticationController(AuthenticationService authenticationService) {
    this.authenticationService = authenticationService;
  }

  @POST
  @Path("/register")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response register(@Valid RegisterDto register) {
    return Response.ok(authenticationService.register(register)).build();
  }

  @POST
  @Path("/authenticate")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response authenticate(
      AuthenticationDto auth) {
    return Response.ok(authenticationService.authenticate(auth)).build();
  }

  @POST
  @Path("/refresh")
  @Produces(MediaType.APPLICATION_JSON)
  public Response refresh(@Context HttpServletRequest request) {
    return Response.ok(authenticationService.refresh(request)).build();
  }
}
