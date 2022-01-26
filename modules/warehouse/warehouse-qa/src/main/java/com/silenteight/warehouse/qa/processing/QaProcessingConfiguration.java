package com.silenteight.warehouse.qa.processing;

import com.silenteight.warehouse.qa.processing.mapping.QaAlertMapper;
import com.silenteight.warehouse.qa.processing.update.QaUpdateService;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.validation.Valid;

@Configuration
@EnableTransactionManagement
@EnableConfigurationProperties(QaProcessingProperties.class)
public class QaProcessingConfiguration {

  @Bean
  QaDataIndexRequestUseCase qaDataIndexRequestUseCase(
      QaAlertMapper qaAlertMapper,
      QaUpdateService qaPersistenceService,
      @Valid QaProcessingProperties properties) {

    return new QaDataIndexRequestUseCase(
        qaAlertMapper,
        qaPersistenceService,
        properties.getQaBatchSize());
  }
}
