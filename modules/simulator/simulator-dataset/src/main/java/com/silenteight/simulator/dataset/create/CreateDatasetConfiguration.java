package com.silenteight.simulator.dataset.create;

import com.silenteight.auditing.bs.AuditingLogger;
import com.silenteight.simulator.dataset.domain.DatasetMetadataService;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;

@Configuration
@EnableConfigurationProperties(CreateDatasetProperties.class)
class CreateDatasetConfiguration {

  @Bean
  CreateDatasetUseCase createDatasetUseCase(
      CreateDatasetService createDatasetService,
      DatasetMetadataService datasetMetadataService,
      @Valid CreateDatasetProperties properties,
      AuditingLogger auditingLogger) {

    return new CreateDatasetUseCase(
        createDatasetService,
        datasetMetadataService,
        properties.datasetLabels(),
        auditingLogger);
  }
}
