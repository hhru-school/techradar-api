package ru.hh.techradar.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.hh.techradar.controller.BlipController;
import ru.hh.techradar.controller.BlipEventController;
import ru.hh.techradar.controller.BlipTemplateController;
import ru.hh.techradar.controller.CompanyController;
import ru.hh.techradar.controller.ContainerController;
import ru.hh.techradar.controller.QuadrantController;
import ru.hh.techradar.controller.RadarController;
import ru.hh.techradar.controller.RadarFileController;
import ru.hh.techradar.controller.RadarVersionController;
import ru.hh.techradar.controller.RingController;
import ru.hh.techradar.controller.UserController;
import ru.hh.techradar.exception.AccessDeniedExceptionMapper;
import ru.hh.techradar.exception.BadCredentialsExceptionMapper;
import ru.hh.techradar.exception.BaseExceptionMapper;
import ru.hh.techradar.exception.ConstraintViolationExceptionMapper;
import ru.hh.techradar.exception.EntityExistsExceptionMapper;
import ru.hh.techradar.exception.FileExceptionMapper;
import ru.hh.techradar.exception.NotFoundExceptionMapper;
import ru.hh.techradar.exception.OperationNotAllowedExceptionMapper;
import ru.hh.techradar.exception.ParamExceptionMapper;
import ru.hh.techradar.exception.UniqueExceptionMapper;
import ru.hh.techradar.security.controller.AuthenticationController;
import ru.hh.techradar.util.DateFormatParamConverterProvider;

@Configuration
@ApplicationPath("/")
public class ContextConfig {

  @Bean
  public ObjectMapper objectMapper() {
    return new ObjectMapper()
        .registerModule(new JavaTimeModule())
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  @Bean
  public ResourceConfig resourceConfig() {
    ResourceConfig resourceConfig = new ResourceConfig();
    resourceConfig.register(BlipController.class);
    resourceConfig.register(BlipEventController.class);
    resourceConfig.register(CompanyController.class);
    resourceConfig.register(ContainerController.class);
    resourceConfig.register(RadarFileController.class);
    resourceConfig.register(QuadrantController.class);
    resourceConfig.register(RadarController.class);
    resourceConfig.register(RadarVersionController.class);
    resourceConfig.register(RingController.class);
    resourceConfig.register(UserController.class);
    resourceConfig.register(AuthenticationController.class);
    resourceConfig.register(BlipTemplateController.class);
    exceptionMapperConfig(resourceConfig);
    converterProviderConfig(resourceConfig);
    return resourceConfig;
  }

  private void exceptionMapperConfig(ResourceConfig resourceConfig) {
    resourceConfig.register(NotFoundExceptionMapper.class);
    resourceConfig.register(BadCredentialsExceptionMapper.class);
    resourceConfig.register(BaseExceptionMapper.class);
    resourceConfig.register(AccessDeniedExceptionMapper.class);
    resourceConfig.register(ParamExceptionMapper.class);
    resourceConfig.register(ConstraintViolationExceptionMapper.class);
    resourceConfig.register(FileExceptionMapper.class);
    resourceConfig.register(EntityExistsExceptionMapper.class);
    resourceConfig.register(UniqueExceptionMapper.class);
    resourceConfig.register(OperationNotAllowedExceptionMapper.class);
  }

  private void converterProviderConfig(ResourceConfig resourceConfig) {
    resourceConfig.register(DateFormatParamConverterProvider.class);
  }
}
