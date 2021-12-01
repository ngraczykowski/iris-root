package com.silenteight.warehouse.retention.production.alert;

import com.silenteight.warehouse.indexer.production.indextracking.ProductionAlertTrackingService;
import com.silenteight.warehouse.indexer.production.indextracking.ProductionMatchTrackingService;
import com.silenteight.warehouse.retention.production.AlertDiscriminatorResolvingService;
import com.silenteight.warehouse.retention.production.MatchDiscriminatorResolvingService;
import com.silenteight.warehouse.retention.production.alert.erasing.DocumentEraseService;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;

@Configuration
@EnableConfigurationProperties(EraseAlertsProperties.class)
class RetentionAlertsConfiguration {

  @Bean
  EraseAlertsUseCase eraseAlertsUseCase(
      AlertDiscriminatorResolvingService alertDiscriminatorResolvingService,
      MatchDiscriminatorResolvingService matchDiscriminatorResolvingService,
      ProductionAlertTrackingService productionAlertTrackingService,
      ProductionMatchTrackingService productionMatchTrackingService,
      DocumentEraseService documentEraseService,
      @Valid EraseAlertsProperties properties) {

    return new EraseAlertsUseCase(
        properties.getBatchSize(),
        alertDiscriminatorResolvingService,
        matchDiscriminatorResolvingService,
        productionAlertTrackingService,
        productionMatchTrackingService,
        documentEraseService);
  }
}
