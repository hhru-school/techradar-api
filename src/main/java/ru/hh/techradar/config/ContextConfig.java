package ru.hh.techradar.config;

import jakarta.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.hh.techradar.controller.BlipController;
import ru.hh.techradar.controller.BlipEventController;
import ru.hh.techradar.controller.CompanyController;
import ru.hh.techradar.controller.QuadrantController;
import ru.hh.techradar.controller.RadarController;
import ru.hh.techradar.controller.RingController;
import ru.hh.techradar.controller.UserController;
import ru.hh.techradar.exception.BaseExceptionMapper;
import ru.hh.techradar.security.controller.TestController;
import ru.hh.techradar.security.exception.BadCredentialsExceptionMapper;
import ru.hh.techradar.exception.ConstraintViolationExceptionMapper;
import ru.hh.techradar.security.exception.ExpiredJwtExceptionMapper;
import ru.hh.techradar.exception.NotFoundExceptionMapper;
import ru.hh.techradar.exception.ParamExceptionMapper;
import ru.hh.techradar.security.controller.AuthenticationController;
import ru.hh.techradar.util.DateFormatParamConverterProvider;

@Configuration
@ApplicationPath("/")
public class ContextConfig {

  @Bean
  public ResourceConfig resourceConfig() {
    ResourceConfig resourceConfig = new ResourceConfig();
//    resourceConfig.packages("ru.hh.techradar.controller");
    resourceConfig.register(BlipController.class);
    resourceConfig.register(BlipEventController.class);
    resourceConfig.register(CompanyController.class);
    resourceConfig.register(QuadrantController.class);
    resourceConfig.register(RadarController.class);
    resourceConfig.register(RingController.class);
    resourceConfig.register(UserController.class);
    resourceConfig.register(AuthenticationController.class);
    resourceConfig.register(TestController.class);
    exceptionMapperConfig(resourceConfig);
    converterProviderConfig(resourceConfig);
    return resourceConfig;
  }

  private void exceptionMapperConfig(ResourceConfig resourceConfig) {
    resourceConfig.register(NotFoundExceptionMapper.class);
    resourceConfig.register(ExpiredJwtExceptionMapper.class);
    resourceConfig.register(BadCredentialsExceptionMapper.class);
    resourceConfig.register(BaseExceptionMapper.class);
    resourceConfig.register(ParamExceptionMapper.class);
    resourceConfig.register(ConstraintViolationExceptionMapper.class);
  }

  private void converterProviderConfig(ResourceConfig resourceConfig) {
    resourceConfig.register(DateFormatParamConverterProvider.class);
  }
}
