package ru.hh.techradar.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import static ru.hh.techradar.controller.UtilService.getUsername;
import ru.hh.techradar.dto.UserDto;
import ru.hh.techradar.filter.UserFilter;
import ru.hh.techradar.mapper.CompanyMapper;
import ru.hh.techradar.mapper.UserMapper;
import ru.hh.techradar.service.UserService;

@Path("/api/users")
public class UserController {

  private final UserService userService;
  private final UserMapper userMapper;
  private final CompanyMapper companyMapper;

  @Inject
  public UserController(
      UserService userService,
      UserMapper userMapper, CompanyMapper companyMapper) {
    this.userService = userService;
    this.userMapper = userMapper;
    this.companyMapper = companyMapper;
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response save(UserDto userDto) {
    return Response
        .ok(userMapper.toDto(userService.save(userMapper.toEntity(userDto))))
        .status(Response.Status.CREATED)
        .build();
  }

  @POST
  @Path("/{username}/companies/{company-id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response joinUserByUsernameAndCompany(
      @PathParam("username") String username,
      @PathParam("company-id") Long companyId
  ) {
    userService.joinUserAndCompany(username, companyId);
    return Response
        .status(Response.Status.CREATED)
        .build();
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response findAllByFilter(@BeanParam UserFilter filter) {
    return Response
        .ok(userMapper.toDtos(userService.findAllByFilter(filter)))
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

  @GET
  @Path("/{id}/companies")
  @Produces(MediaType.APPLICATION_JSON)
  public Response findAllCompaniesByUserId(@PathParam("id") Long id) {
    return Response
        .ok(companyMapper.toDtos(userService.findAllCompaniesByUserId(id)))
        .build();
  }

  @GET
  @Path("/companies")
  @Produces(MediaType.APPLICATION_JSON)
  public Response findAllCompaniesByCookiesUserId() {
    return Response
        .ok(companyMapper.toDtos(userService.findAllCompaniesByUsername(getUsername())))
        .build();
  }

  @PUT
  @Path("/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response update(@PathParam("id") Long id, UserDto userDto) {
    return Response
        .ok(userMapper.toDto(userService.update(id, userMapper.toEntity(userDto))))
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

  @DELETE
  @Path("/{username}/companies/{company-id}")
  public Response disjointUserAndCompany(
      @PathParam("username") String userName,
      @PathParam("company-id") Long companyId
  ) {
    userService.disjointUserAndCompany(userName, companyId);
    return Response
        .status(Response.Status.NO_CONTENT)
        .build();
  }
}
