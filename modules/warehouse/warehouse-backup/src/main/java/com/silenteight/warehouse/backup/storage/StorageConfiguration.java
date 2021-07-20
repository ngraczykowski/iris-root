package com.silenteight.warehouse.backup.storage;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class StorageConfiguration {

  @Bean
  StorageService storageService() {
    return new StorageService();
  }
}
