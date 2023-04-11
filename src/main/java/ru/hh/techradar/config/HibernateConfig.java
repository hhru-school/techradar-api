package ru.hh.techradar.config;

import java.util.Properties;
import javax.sql.DataSource;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import ru.hh.techradar.entity.Blip;
import ru.hh.techradar.entity.BlipEvent;
import ru.hh.techradar.entity.Company;
import ru.hh.techradar.entity.Quadrant;
import ru.hh.techradar.entity.QuadrantSetting;
import ru.hh.techradar.entity.Radar;
import ru.hh.techradar.entity.Ring;
import ru.hh.techradar.entity.RingSetting;
import ru.hh.techradar.entity.User;

@Configuration
public class HibernateConfig {

  @Value("${db.url}")
  private String URL;
  @Value("${db.username}")
  private String USERNAME;
  @Value("${db.password}")
  private String PASSWORD;
  @Value("${db.show-sql}")
  private String SHOW_SQL;
  @Value("${db.dialect}")
  private String DIALECT;
  @Value("${db.hbm2ddl-auto}")
  private String HBM2DDL_AUTO;

  @Bean
  public DataSource dataSource() {
    return new DriverManagerDataSource(URL, USERNAME, PASSWORD);
  }

  @Bean
  public LocalSessionFactoryBean localSessionFactoryBean(DataSource dataSource) {
    LocalSessionFactoryBean localSessionFactoryBean = new LocalSessionFactoryBean();
    localSessionFactoryBean.setDataSource(dataSource);
    localSessionFactoryBean.setAnnotatedClasses(
        Company.class,
        BlipEvent.class,
        Blip.class,
        RingSetting.class,
        Ring.class,
        QuadrantSetting.class,
        Quadrant.class,
        Radar.class,
        User.class
    );

    Properties properties = new Properties();
    properties.put(Environment.DIALECT, DIALECT);
    properties.put(Environment.SHOW_SQL, SHOW_SQL);
    properties.put(Environment.HBM2DDL_AUTO, HBM2DDL_AUTO);

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
}
