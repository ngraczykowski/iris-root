package com.silenteight.warehouse.production.handler.v2;

import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.warehouse.production.handler.ProductionIndexerProperties;
import com.silenteight.warehouse.production.persistence.insert.PersistenceService;
import com.silenteight.warehouse.production.persistence.mapping.alert.AlertMapper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;

@Configuration
@RequiredArgsConstructor
class ProductionAlertV2Configuration {

  @Bean
  ProductionAlertV2UseCase productionAlertV2UseCase(
      AlertMapper alertMapper,
      PersistenceService persistenceService,
      @Valid ProductionIndexerProperties properties,
      TimeSource timeSource) {

    return new ProductionAlertV2UseCase(
        alertMapper,
        persistenceService,
        timeSource,
        properties.getProductionBatchSize());
  }
}
