package com.silenteight.warehouse.backup.indexing;

import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.backup.storage.StorageService;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(IndexerProperties.class)
public class IndexingConfiguration {

  @Bean
  IndexingUseCase indexingUseCase(StorageService storageService) {
    return new IndexingUseCase(storageService);
  }
}
