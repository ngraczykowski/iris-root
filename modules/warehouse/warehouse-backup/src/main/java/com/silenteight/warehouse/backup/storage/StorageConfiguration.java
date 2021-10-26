package com.silenteight.warehouse.backup.storage;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories
@EntityScan
@EnableConfigurationProperties(DiagnosticProperties.class)
class StorageConfiguration {

  @Bean
  StorageService storageService(
      DiagnosticProperties diagnosticProperties,
      BackupMessageRepository backupMessageRepository) {
    return new StorageService(backupMessageRepository, diagnosticProperties.enabled);
  }
}
