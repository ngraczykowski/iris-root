package com.silenteight.warehouse.backup.storage;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories
@EntityScan
class StorageConfiguration {

  @Bean
  StorageService storageService(BackupMessageRepository backupMessageRepository) {
    return new StorageService(backupMessageRepository);
  }
}
