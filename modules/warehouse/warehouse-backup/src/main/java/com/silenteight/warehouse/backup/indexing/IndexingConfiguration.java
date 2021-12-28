package com.silenteight.warehouse.backup.indexing;

import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.backup.indexing.listener.ProductionIndexRequestV1BackupCommandHandler;
import com.silenteight.warehouse.backup.indexing.listener.ProductionIndexRequestV2BackupCommandHandler;
import com.silenteight.warehouse.backup.storage.StorageService;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(IndexerProperties.class)
public class IndexingConfiguration {

  @Bean
  ProductionIndexRequestV1BackupCommandHandler productionIndexRequestV1BackupCommandHandler(
      StorageService storageService) {
    return new BackupUseCaseV1(storageService);
  }

  @Bean
  ProductionIndexRequestV2BackupCommandHandler productionIndexRequestV2BackupCommandHandler(
      StorageService storageService) {
    return new BackupUseCaseV2(storageService);
  }
}
