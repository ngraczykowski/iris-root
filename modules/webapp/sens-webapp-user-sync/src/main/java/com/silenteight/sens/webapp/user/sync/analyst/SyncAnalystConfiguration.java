package com.silenteight.sens.webapp.user.sync.analyst;

import com.silenteight.sens.webapp.user.UserListQuery;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
@ConditionalOnProperty(value = "user.sync.analyst.enabled")
class SyncAnalystConfiguration {

  @Bean
  SyncAnalystService syncAnalystService(
      UserListQuery userListQuery, ExternalAnalystRepository repository) {

    return new SyncAnalystService(userListQuery, repository);
  }

  @Bean
  ExternalAnalystRepository databaseExternalAnalystRepository(
      @Qualifier("externalDataSource") DataSource externalDataSource,
      SyncAnalystProperties syncAnalystProperties) {

    return new DatabaseExternalAnalystRepository(
        syncAnalystProperties.getUserDbRelationName(), new JdbcTemplate(externalDataSource));
  }

  @Bean
  @ConfigurationProperties(prefix = "user.sync.analyst.datasource.hikari")
  DataSource externalDataSource() {
    return createHikariDataSource(externalDataSourceProperties());
  }

  @Bean
  @ConfigurationProperties(prefix = "user.sync.analyst.datasource")
  DataSourceProperties externalDataSourceProperties() {
    return new DataSourceProperties();
  }

  private static HikariDataSource createHikariDataSource(DataSourceProperties properties) {
    return properties
        .initializeDataSourceBuilder()
        .type(HikariDataSource.class)
        .build();
  }

  @Bean
  @ConfigurationProperties(prefix = "user.sync.analyst")
  SyncAnalystProperties syncAnalystProperties() {
    return new SyncAnalystProperties();
  }
}
