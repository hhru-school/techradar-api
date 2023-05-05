package ru.hh.techradar.config;

import jakarta.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.hh.techradar.exception.ConstraintViolationExceptionMapper;
import ru.hh.techradar.exception.NotFoundExceptionMapper;
import ru.hh.techradar.exception.ParamExceptionMapper;
import ru.hh.techradar.util.DateFormatParamConverterProvider;

@Configuration
@ApplicationPath("/")
public class ContextConfig {

  @Bean
  public ResourceConfig resourceConfig() {
    ResourceConfig resourceConfig = new ResourceConfig();
    resourceConfig.packages("ru.hh.techradar.controller");
    exceptionMapperConfig(resourceConfig);
    converterProviderConfig(resourceConfig);
    return resourceConfig;
  }

  private void exceptionMapperConfig(ResourceConfig resourceConfig) {
    resourceConfig.register(NotFoundExceptionMapper.class);
    resourceConfig.register(ParamExceptionMapper.class);
    resourceConfig.register(ConstraintViolationExceptionMapper.class);
  }

  private void converterProviderConfig(ResourceConfig resourceConfig) {
    resourceConfig.register(DateFormatParamConverterProvider.class);
  }
}
