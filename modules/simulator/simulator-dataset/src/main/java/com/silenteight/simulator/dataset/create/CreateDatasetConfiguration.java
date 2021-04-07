package com.silenteight.simulator.dataset.create;

import com.silenteight.auditing.bs.AuditingLogger;
import com.silenteight.simulator.dataset.domain.DatasetMetadataService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class CreateDatasetConfiguration {

  @Bean
  CreateDatasetUseCase createDatasetUseCase(
      CreateDatasetService createDatasetService,
      DatasetMetadataService datasetMetadataService,
      AuditingLogger auditingLogger) {

    return new CreateDatasetUseCase(createDatasetService, datasetMetadataService, auditingLogger);
  }
}
