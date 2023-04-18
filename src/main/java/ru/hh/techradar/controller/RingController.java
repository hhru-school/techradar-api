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
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import ru.hh.techradar.dto.RingDto;
import ru.hh.techradar.entity.Ring;
import ru.hh.techradar.mapper.RingMapper;
import ru.hh.techradar.service.RingService;

@Path("/api/radars")
public class RingController {
  private final RingMapper ringMapper;
  private final RingService ringService;

  @Inject
  public RingController(RingMapper ringMapper, RingService ringService) {
    this.ringMapper = ringMapper;
    this.ringService = ringService;
  }

  @GET
  @Path("/{radarId}/{date}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getRingsByDateAndRadar(@PathParam("date") String dateString, @PathParam("radarId") Long radarId) {
    Instant filterDate = parseInstantString(dateString).orElseThrow();
    List<Ring> currentRings = ringService.fetchRingsByRadarId(radarId, filterDate);
    return Response.ok(ringMapper.toDtosByDate(currentRings, filterDate)).build();
  }

  @POST
  @Path("/{radarId}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response addRing(@PathParam("radarId") Long radarId, RingDto ringDto) {
    return Response.ok(
        ringMapper.toDtoByDate(
            ringService.save(ringMapper.toEntity(ringDto, radarId)), Instant.now()
        )
    ).build();
  }

  @PUT
  @Path("/{radarId}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response updateRing(@PathParam("radarId") Long radarId, RingDto ringDto) {
    Ring newRing = ringService.update(ringDto.getId(), ringMapper.toEntity(ringDto, radarId));
    return Response.ok(
        ringMapper.toDtoByDate(newRing, Instant.now())
    ).build();
  }

  @DELETE
  @Path("/{date}")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response markRingAsRemoved(@PathParam("date") String dateString, RingDto ringDto) {
    Instant date = parseInstantString(dateString).orElseThrow();
    Ring ring = ringService.findById(ringDto.getId());
    ring.setRemovedAt(date);
    ringService.save(ring);
    return Response.ok().build();
  }

  private Optional<Instant> parseInstantString(String date) {
    try {
      return Optional.of(Instant.parse(date));
    } catch (Exception ex) {
      return Optional.empty();
    }
  }
}
