package com.silenteight.sens.webapp.scb.user.sync.analyst;

import com.silenteight.sens.webapp.audit.trace.AuditTracer;
import com.silenteight.sens.webapp.scb.user.sync.analyst.bulk.BulkAnalystService;
import com.silenteight.sens.webapp.user.lock.LockUserUseCase;
import com.silenteight.sens.webapp.user.lock.UnlockUserUseCase;
import com.silenteight.sens.webapp.user.registration.RegisterExternalUserUseCase;
import com.silenteight.sens.webapp.user.update.AddRolesToUserUseCase;
import com.silenteight.sens.webapp.user.update.UpdateUserDisplayNameUseCase;
import com.silenteight.sep.usermanagement.api.UserListQuery;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
@ComponentScan
@ConditionalOnProperty(value = "user.sync.analyst.enabled")
class SyncAnalystConfiguration {

  @Bean
  SyncAnalystsUseCase syncAnalystsUseCase(
      UserListQuery userListQuery,
      ExternalAnalystRepository externalAnalystRepository,
      BulkAnalystService bulkAnalystService,
      AuditTracer auditTracer,
      SyncAnalystProperties syncAnalystProperties) {

    return new SyncAnalystsUseCase(
        userListQuery,
        externalAnalystRepository,
        new AnalystSynchronizer(),
        bulkAnalystService,
        auditTracer,
        syncAnalystProperties.getMaxErrors());
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

  @Bean
  BulkAnalystService analystService(
      RegisterExternalUserUseCase registerExternalUserUseCase,
      UnlockUserUseCase unlockUserUseCase,
      AddRolesToUserUseCase addRolesToUserUseCase,
      UpdateUserDisplayNameUseCase updateUserDisplayNameUseCase,
      LockUserUseCase lockUserUseCase) {

    return new BulkAnalystService(
        registerExternalUserUseCase,
        unlockUserUseCase,
        addRolesToUserUseCase,
        updateUserDisplayNameUseCase,
        lockUserUseCase);
  }
}
