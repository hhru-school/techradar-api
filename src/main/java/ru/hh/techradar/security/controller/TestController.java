package ru.hh.techradar.security.controller;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import ru.hh.techradar.security.model.CustomUserDetails;
import ru.hh.techradar.security.model.PrincipalUser;

@Controller
@Path("/api/tests")
public class TestController {

  @GET
  @Path("/admin")
  @Produces(MediaType.TEXT_PLAIN)
  @PreAuthorize("hasAuthority('ADMIN')")
  public Response helloAdmin() {
    return Response
        .ok("Hello, ADMIN!")
        .build();
  }

  @GET
  @Path("/user")
  @Produces(MediaType.TEXT_PLAIN)
  @PreAuthorize("hasAuthority('USER')")
  public Response helloUser() {
    return Response
        .ok("Hello, USER!")
        .build();
  }

  @GET
  @Path("/security/admin")
  @Produces(MediaType.TEXT_PLAIN)
  public Response securityHelloAdmin() {
    return Response
        .ok("Security hello, ADMIN!")
        .build();
  }

  @GET
  @Path("/security/user")
  @Produces(MediaType.TEXT_PLAIN)
  public Response securityHelloUser() {
    return Response
        .ok("Security hello, USER!")
        .build();
  }

  @GET
  @Path("/get/user")
  @Produces(MediaType.TEXT_PLAIN)
  public Response getUser(@PrincipalUser CustomUserDetails userDetails) {
    return Response
        .ok(userDetails)
        .build();
  }
}
