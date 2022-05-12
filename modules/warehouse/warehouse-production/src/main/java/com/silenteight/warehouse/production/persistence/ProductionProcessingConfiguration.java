package com.silenteight.warehouse.production.persistence;

import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.warehouse.production.persistence.insert.PersistenceService;
import com.silenteight.warehouse.production.persistence.mapping.alert.AlertMapper;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;

@Configuration
@EnableConfigurationProperties(ProductionPersistenceProperties.class)
public class ProductionProcessingConfiguration {

  @Bean
  ProductionAlertV1UseCase productionAlertV1UseCase(
      AlertMapper alertMapper,
      PersistenceService persistenceService,
      @Valid ProductionPersistenceProperties properties,
      TimeSource timeSource) {

    return new ProductionAlertV1UseCase(
        alertMapper,
        persistenceService,
        timeSource,
        properties.getProductionBatchSize());
  }

  @Bean
  ProductionAlertV2UseCase productionAlertV2UseCase(
      AlertMapper alertMapper,
      PersistenceService persistenceService,
      @Valid ProductionPersistenceProperties properties,
      TimeSource timeSource) {

    return new ProductionAlertV2UseCase(
        alertMapper,
        persistenceService,
        timeSource,
        properties.getProductionBatchSize());
  }
}
