package ru.hh.techradar.config;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.Provider;
import java.time.Instant;
import java.util.Objects;

@Provider
public class PreRequestFilter implements ContainerRequestFilter {
  @Override
  public void filter(ContainerRequestContext requestContext) {
    MultivaluedMap<String, String> params = requestContext.getUriInfo().getQueryParameters();
    if (Objects.nonNull(params.get("actualDate"))) {
      for (String param : params.get("actualDate")) {
        Instant.parse(param);
      }
    }
  }
}
