package com.silenteight.simulator.dataset.archive;

import com.silenteight.auditing.bs.AuditingLogger;
import com.silenteight.simulator.dataset.domain.DatasetMetadataService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ArchiveDatasetConfiguration {

  @Bean
  ArchiveDatasetUseCase archiveDatasetUseCase(
      DatasetMetadataService datasetMetadataService, AuditingLogger auditingLogger) {

    return new ArchiveDatasetUseCase(datasetMetadataService, auditingLogger);
  }
}
