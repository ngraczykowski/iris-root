package com.silenteight.sens.webapp.user.sync.analyst;

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
      AnalystQuery analystQuery, ExternalAnalystRepository repository) {

    return new SyncAnalystService(analystQuery, repository);
  }

  @Bean
  AnalystQuery analystQuery(AnalystQueryRepository repository) {
    return new AnalystQuery(repository);
  }

  @Bean
  AnalystQueryRepository databaseAnalystQueryRepository() {
    return new DatabaseAnalystQueryRepository();
  }

  @Bean
  ExternalAnalystRepository databaseExternalExternalAnalystRepository(
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
  SyncAnalystProperties analystSyncProperties() {
    return new SyncAnalystProperties();
  }
}
