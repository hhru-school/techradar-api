package ru.hh.techradar.controller;

import jakarta.inject.Inject;
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
import ru.hh.techradar.dto.UserDto;
import ru.hh.techradar.mapper.UserMapper;
import ru.hh.techradar.service.UserService;

@Path("/api/users")
public class UserController {

  private final UserService userService;
  private final UserMapper userMapper;

  @Inject
  public UserController(
      UserService userService,
      UserMapper userMapper
  ) {
    this.userService = userService;
    this.userMapper = userMapper;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response findAllByFilter(@QueryParam("companyId") Long companyId) {
    return Response
        .ok(userMapper.toDtos(userService.findAllByFilter(companyId)))
        .build();
  }

  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response findById(@PathParam("id") Long id) {
    return Response
        .ok(userMapper.toDto(userService.findById(id)))
        .build();
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response save(@QueryParam("companyId") Long companyId, UserDto userDto) {
    return Response
        .ok(userMapper.toDto(userService.save(companyId, userMapper.toEntity(userDto))))
        .status(Response.Status.CREATED)
        .build();
  }

  @PUT
  @Path("/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response update(@PathParam("id") Long id, @QueryParam("companyId") Long companyId, UserDto userDto) {
    return Response
        .ok(userMapper.toDto(userService.update(id, companyId, userMapper.toEntity(userDto))))
        .build();
  }

  @DELETE
  @Path("/{id}")
  public Response deleteById(@PathParam("id") Long id) {
    userService.deleteById(id);
    return Response
        .status(Response.Status.NO_CONTENT)
        .build();
  }
}
