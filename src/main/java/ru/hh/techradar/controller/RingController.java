package ru.hh.techradar.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.time.Instant;
import java.util.List;
import ru.hh.techradar.dto.RingDto;
import ru.hh.techradar.entity.Ring;
import ru.hh.techradar.entity.RingSetting;
import ru.hh.techradar.mapper.RingMapperUnited;
import ru.hh.techradar.service.RadarService;
import ru.hh.techradar.service.RingService;
import ru.hh.techradar.service.RingServiceUnited;
import ru.hh.techradar.util.Pair;

@Path("/api/rings")
public class RingController {
  private final RingMapperUnited ringMapperUnited;
  private final RingService ringService;
  private final RingServiceUnited ringServiceUnited;
  private final RadarService radarService;

  @Inject
  public RingController(
      RingMapperUnited ringMapperUnited, RingService ringService, RingServiceUnited ringServiceUnited,
      RadarService radarService
  ) {
    this.ringMapperUnited = ringMapperUnited;
    this.ringService = ringService;
    this.ringServiceUnited = ringServiceUnited;
    this.radarService = radarService;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response getRingsByDateAndRadar(@QueryParam("date") Instant date, @QueryParam("radarId") Long radarId) {
    List<Pair<Ring, RingSetting>> currentPairs = ringServiceUnited.fetchPairsByRadarIdAndDate(radarId, date);
    return Response.ok(ringMapperUnited.toDtos(currentPairs)).build();
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response addRing(@QueryParam("radarId") Long radarId, RingDto ringDto) {
    var newRing = ringMapperUnited.toEntity(ringDto);
    newRing.getFirst().setRadar(radarService.findById(radarId));
    return Response.ok(
        ringMapperUnited.toDto(
            ringServiceUnited.save(newRing)
        )
    ).build();
  }

  @Path("/force")
  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response forceUpdateRing(@QueryParam("radarId") Long radarId, RingDto ringDto) {
    var newRing = ringMapperUnited.toEntity(ringDto);
    newRing.getFirst().setRadar(radarService.findById(radarId));
    var newPair = ringServiceUnited.forceUpdate(ringDto.getId(), newRing);
    return Response.ok(
        ringMapperUnited.toDto(newPair)
    ).build();
  }

  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response updateRing(@QueryParam("radarId") Long radarId, RingDto ringDto) {
    var newRing = ringMapperUnited.toEntity(ringDto);
    newRing.getFirst().setRadar(radarService.findById(radarId));
    var newPair = ringServiceUnited.update(ringDto.getId(), newRing);
    return Response.ok(
        ringMapperUnited.toDto(newPair)
    ).build();
  }

  @DELETE
  @Consumes(MediaType.APPLICATION_JSON)
  public Response markRingAsRemoved(@QueryParam("date") Instant date, RingDto ringDto) {
    Ring ring = ringService.findById(ringDto.getId());
    ring.setRemovedAt(date);
    ringService.update(ringDto.getId(), ring);
    return Response.ok().build();
  }
}
