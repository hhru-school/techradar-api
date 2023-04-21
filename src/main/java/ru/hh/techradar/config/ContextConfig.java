package ru.hh.techradar.config;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.hh.techradar.controller.CompanyController;
import ru.hh.techradar.controller.QuadrantController;
import ru.hh.techradar.controller.RadarController;
import ru.hh.techradar.controller.RingController;
import ru.hh.techradar.exception.DateParseExceptionMapper;
import ru.hh.techradar.exception.NotFoundExceptionMapper;
import ru.hh.techradar.util.DateFormatParamConverterProvider;

@Configuration
public class ContextConfig {

  @Bean
  public ResourceConfig resourceConfig() {
    ResourceConfig resourceConfig = new ResourceConfig();
    resourceConfig.register(CompanyController.class);
    resourceConfig.register(QuadrantController.class);
    resourceConfig.register(NotFoundExceptionMapper.class);
    resourceConfig.register(DateParseExceptionMapper.class);
    resourceConfig.register(DateFormatParamConverterProvider.class);
    resourceConfig.register(PreRequestFilter.class);
    resourceConfig.register(RingController.class);
    resourceConfig.register(RadarController.class);
    return resourceConfig;
  }
}
