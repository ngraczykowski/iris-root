package com.silenteight.warehouse.migration.backupmessage;

import com.silenteight.warehouse.production.handler.ProductionRequestV1CommandHandler;
import com.silenteight.warehouse.production.handler.ProductionRequestV2CommandHandler;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.jdbc.lock.DefaultLockRepository;
import org.springframework.integration.jdbc.lock.JdbcLockRegistry;
import org.springframework.integration.jdbc.lock.LockRepository;
import org.springframework.integration.support.locks.LockRegistry;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import javax.sql.DataSource;

@Configuration
@ConditionalOnProperty(value = "warehouse.alert.migration.enabled", havingValue = "true")
@EnableConfigurationProperties(MigrationProperties.class)
class BackupMessageMigrationConfiguration {

  private static final String LOCK_TABLE_PREFIX = "warehouse_";

  @Bean
  BackupMessageMigration migrationWarehouseBackupProcess(
      MigrationProperties migrationProperties,
      BackupMessageQuery backupMessageQuery,
      LockRegistry jdbcLockRegistry,
      RabbitMessageContainerLifecycle rabbitMessageContainerLifecycle,
      TransactionTemplate transactionTemplate,
      ProductionRequestV1CommandHandler productionRequestV1CommandHandler,
      ProductionRequestV2CommandHandler productionRequestV2CommandHandler
  ) {

    return new BackupMessageMigration(
        migrationProperties,
        backupMessageQuery,
        jdbcLockRegistry,
        rabbitMessageContainerLifecycle,
        transactionTemplate,
        productionRequestV1CommandHandler,
        productionRequestV2CommandHandler);
  }

  @Bean
  DefaultLockRepository defaultLockRepository(DataSource dataSource) {
    DefaultLockRepository defaultLockRepository = new DefaultLockRepository(dataSource);
    defaultLockRepository.setPrefix(LOCK_TABLE_PREFIX);
    return defaultLockRepository;
  }

  @Bean
  JdbcLockRegistry jdbcLockRegistry(LockRepository lockRepository) {
    return new JdbcLockRegistry(lockRepository);
  }

  @Bean
  RabbitMessageContainerLifecycle rabbitMqListenersHandler(
      List<IntegrationFlow> queueBeans) {
    return new RabbitMessageContainerLifecycle(queueBeans);
  }

  @Bean
  BackupMessageQuery backupMessageJdbcRepository(JdbcTemplate jdbcTemplate) {
    return new BackupMessageQuery(jdbcTemplate);
  }

  @Bean
  TransactionTemplate transactionTemplate(PlatformTransactionManager transactionManager) {
    return new TransactionTemplate(transactionManager);
  }
}
