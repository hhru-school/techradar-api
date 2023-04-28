package ru.hh.techradar.config;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.hh.techradar.controller.CompanyController;
import ru.hh.techradar.controller.UserController;
import ru.hh.techradar.exception.EntityExistsExceptionMapper;
import ru.hh.techradar.exception.NotFoundExceptionMapper;

@Configuration
public class ContextConfig {

  @Bean
  public ResourceConfig resourceConfig() {
    ResourceConfig resourceConfig = new ResourceConfig();
    resourceConfig.register(CompanyController.class);
    resourceConfig.register(UserController.class);
    resourceConfig.register(NotFoundExceptionMapper.class);
    resourceConfig.register(EntityExistsExceptionMapper.class);
    return resourceConfig;
  }
}
