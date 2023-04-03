package ru.hh.techradar.config;

import java.util.Properties;
import javax.sql.DataSource;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletProperties;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Environment;
import org.hibernate.dialect.PostgreSQLDialect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import ru.hh.techradar.entity.Company;
import ru.hh.techradar.exception.maper.NotFoundExceptionMapper;
import ru.hh.techradar.resource.CompanyResource;

@Configuration
public class AppConfig {

  @Value("${db.url}")
  private String url;
  @Value("${db.username}")
  private String username;
  @Value("${db.password}")
  private String password;

  @Bean
  public DataSource dataSource() {
    return new DriverManagerDataSource(url, username, password);
  }

  @Bean
  LocalSessionFactoryBean localSessionFactoryBean(DataSource dataSource) {
    LocalSessionFactoryBean localSessionFactoryBean = new LocalSessionFactoryBean();
    localSessionFactoryBean.setDataSource(dataSource);
    localSessionFactoryBean.setPackagesToScan("ru.hh.techradar.repository");
    localSessionFactoryBean.setAnnotatedClasses(Company.class);

    Properties properties = new Properties();
    properties.put(Environment.DIALECT, PostgreSQLDialect.class.getName());
    properties.put(Environment.SHOW_SQL, true);
    properties.put(Environment.HBM2DDL_AUTO, "update");
    localSessionFactoryBean.setHibernateProperties(properties);
    return localSessionFactoryBean;
  }

  @Bean
  public SessionFactory sessionFactory(LocalSessionFactoryBean localSessionFactoryBean) {
    return localSessionFactoryBean.getObject();
  }

  @Bean
  public HibernateTransactionManager platformTransactionManager(SessionFactory sessionFactory) {
    return new HibernateTransactionManager(sessionFactory);
  }

  @Bean
  public ResourceConfig resourceConfig() {
    ResourceConfig resourceConfig = new ResourceConfig();
    resourceConfig.register(CompanyResource.class);
    resourceConfig.register(NotFoundExceptionMapper.class);
    resourceConfig.property(ServletProperties.FILTER_FORWARD_ON_404, true);
    return resourceConfig;
  }

}
