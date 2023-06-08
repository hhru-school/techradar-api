package ru.hh.techradar.controller;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.CookieParam;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.stereotype.Controller;
import ru.hh.techradar.dto.CompanyDto;
import ru.hh.techradar.entity.Company;
import ru.hh.techradar.entity.User;
import ru.hh.techradar.mapper.CompanyMapper;
import ru.hh.techradar.service.CompanyService;
import ru.hh.techradar.service.UserService;

@Controller
@Path("/api/companies")
public class CompanyController {

  private final CompanyMapper companyMapper;
  private final CompanyService companyService;
  private final UserService userService;

  public CompanyController(
      CompanyMapper companyMapper,
      CompanyService companyService,
      UserService userService) {
    this.companyMapper = companyMapper;
    this.companyService = companyService;
    this.userService = userService;
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response saveAndJoinUser(CompanyDto companyDto,
      @CookieParam("username") String username) {
    Company company = companyService.save(companyMapper.toEntity(companyDto));//TODO: return here after role understanding
    User user = userService.findByUsername(username);
    userService.joinUserAndCompany(user.getId(), company.getId());
    return Response
        .ok(companyMapper.toDto(company))
        .status(Response.Status.CREATED)
        .build();
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response findAll() {
    return Response
        .ok(companyMapper.toDtos(companyService.findAll()))
        .build();
  }

  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response findById(@PathParam("id") Long id) {
    return Response
        .ok(companyMapper.toDto(companyService.findById(id)))
        .build();
  }

  @PUT
  @Path("/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response update(@PathParam("id") Long id, CompanyDto companyDto) {
    return Response
        .ok(companyMapper.toDto(companyService.update(id, companyMapper.toEntity(companyDto))))
        .build();
  }

  @DELETE
  @Path("/{id}")
  public Response deleteById(@PathParam("id") Long id) {
    companyService.deleteById(id);
    return Response
        .status(Response.Status.NO_CONTENT)
        .build();
  }
}
