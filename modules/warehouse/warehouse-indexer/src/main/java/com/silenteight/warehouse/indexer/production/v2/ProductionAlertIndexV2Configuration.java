package com.silenteight.warehouse.indexer.production.v2;

import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.warehouse.indexer.alert.indexing.AlertIndexService;
import com.silenteight.warehouse.indexer.alert.mapping.AlertMapper;
import com.silenteight.warehouse.indexer.match.mapping.MatchMapper;
import com.silenteight.warehouse.indexer.production.ProductionIndexerProperties;
import com.silenteight.warehouse.indexer.production.indextracking.ProductionAlertTrackingService;
import com.silenteight.warehouse.indexer.production.indextracking.ProductionMatchTrackingService;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(ProductionIndexerProperties.class)
class ProductionAlertIndexV2Configuration {

  @Bean
  ProductionAlertIndexV2UseCase productionAlertIndexV2UseCase(
      ProductionAlertIndexResolvingService productionAlertIndexResolvingV2Service,
      ProductionAlertMappingService productionAlertMappingV2Service,
      AlertIndexService alertIndexService,
      ProductionIndexerProperties properties,
      TimeSource timeSource) {

    return new ProductionAlertIndexV2UseCase(
        productionAlertIndexResolvingV2Service,
        productionAlertMappingV2Service,
        alertIndexService,
        timeSource,
        properties.getProductionBatchSize());
  }

  @Bean
  ProductionAlertIndexResolvingService productionAlertIndexResolvingV2Service(
      ProductionAlertTrackingService productionAlertTrackingService,
      ProductionMatchTrackingService productionMatchTrackingService) {

    return new ProductionAlertIndexResolvingService(
        productionAlertTrackingService, productionMatchTrackingService);
  }

  @Bean
  ProductionAlertMappingService productionAlertMappingV2Service(
      AlertMapper alertMapper, MatchMapper matchMapper) {

    return new ProductionAlertMappingService(alertMapper, matchMapper);
  }
}
