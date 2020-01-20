package com.silenteight.sens.webapp.backend.config;

import com.silenteight.sens.webapp.backend.rest.FrontendSettingsController;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class WebApplicationConfiguration {

  @Bean
  @ConfigurationProperties(prefix = "sens.web")
  WebApplicationProperties webApplicationProperties() {
    return new WebApplicationProperties();
  }

  @Bean
  FrontendSettingsController frontendSettingsController() {
    return new FrontendSettingsController();
  }

  @Bean
  @Primary
  @ConfigurationProperties(prefix = "spring.datasource")
  public DataSourceProperties dataSourceProperties() {
    return new DataSourceProperties();
  }

  @Bean
  @Primary
  @ConfigurationProperties(prefix = "spring.datasource.hikari")
  public DataSource dataSource() {
    return dataSourceProperties()
        .initializeDataSourceBuilder()
        .type(HikariDataSource.class)
        .build();
  }
}
