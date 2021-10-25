package com.silenteight.warehouse.indexer.production.v1;

import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.warehouse.indexer.alert.indexing.AlertIndexService;
import com.silenteight.warehouse.indexer.alert.mapping.AlertMapper;
import com.silenteight.warehouse.indexer.production.ProductionIndexerProperties;
import com.silenteight.warehouse.indexer.production.indextracking.ProductionAlertTrackingService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class ProductionAlertIndexV1Configuration {

  @Bean
  ProductionAlertIndexV1UseCase productionAlertIndexV1UseCase(
      ProductionAlertIndexResolvingService productionAlertIndexResolvingV1Service,
      ProductionAlertMappingService productionAlertMappingV1Service,
      AlertIndexService alertIndexService,
      ProductionIndexerProperties properties,
      TimeSource timeSource) {

    return new ProductionAlertIndexV1UseCase(
        productionAlertIndexResolvingV1Service,
        productionAlertMappingV1Service,
        alertIndexService,
        timeSource,
        properties.getProductionBatchSize());
  }

  @Bean
  ProductionAlertIndexResolvingService productionAlertIndexResolvingV1Service(
      ProductionAlertTrackingService productionAlertTrackingService) {

    return new ProductionAlertIndexResolvingService(productionAlertTrackingService);
  }

  @Bean
  ProductionAlertMappingService productionAlertMappingV1Service(AlertMapper alertMapper) {
    return new ProductionAlertMappingService(alertMapper);
  }
}
