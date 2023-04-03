package ru.hh.techradar.resource;

import jakarta.inject.Inject;
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
import java.util.ArrayList;
import java.util.List;
import ru.hh.techradar.dto.CompanyDto;
import ru.hh.techradar.entity.Company;
import ru.hh.techradar.mapper.CompanyMapper;
import ru.hh.techradar.service.company.CompanyService;

@Path("/api/companies")
public class CompanyResource {

  private final CompanyMapper companyMapper;
  private final CompanyService companyService;

  @Inject
  public CompanyResource(CompanyMapper companyMapper,
                         CompanyService companyService) {
    this.companyMapper = companyMapper;
    this.companyService = companyService;
  }


  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public List<Company> findAll() {
    return new ArrayList<>();
  }

  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response findById(@PathParam("id") Long id) {
    return Response
        .ok(companyMapper.toCompanyDto(companyService.findById(id)))
        .build();
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response save(CompanyDto companyDto) {
    return Response
        .ok(companyService.save(companyMapper.toCompany(companyDto)))
        .build();
  }

  @PUT
  @Path("/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response update(@PathParam("id") Long id, CompanyDto companyDto) {
    return Response
        .ok(companyMapper.toCompanyDto(companyService.update(id, companyMapper.toCompany(companyDto))))
        .build();
  }

  @DELETE
  @Path("/{id}")
  @Produces(MediaType.TEXT_PLAIN)
  public Response deleteById(@PathParam("id") Long id) {
    companyService.deleteById(id);
    return Response
        .ok("ok")
        .build();
  }

}
