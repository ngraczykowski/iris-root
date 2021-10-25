package com.silenteight.warehouse.indexer.production.indextracking;

import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.warehouse.common.environment.EnvironmentProperties;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.validation.Valid;

@EntityScan
@EnableJpaRepositories
@Configuration
class IndexTrackingConfiguration {

  @Bean
  ProductionAlertTrackingService productionAlertTrackingService(
      ProductionAlertRepository productionAlertRepository,
      TimeSource timeSource,
      @Valid EnvironmentProperties environmentProperties) {

    ProductionAlertNamingStrategy productionNamingStrategy =
        new ProductionAlertNamingStrategy(timeSource, environmentProperties.getPrefix());

    return new ProductionAlertTrackingService(productionAlertRepository, productionNamingStrategy);
  }

  @Bean
  ProductionMatchTrackingService productionMatchTrackingService(
      ProductionMatchRepository productionMatchRepository,
      TimeSource timeSource,
      @Valid EnvironmentProperties environmentProperties) {

    ProductionMatchNamingStrategy productionNamingStrategy =
        new ProductionMatchNamingStrategy(timeSource, environmentProperties.getPrefix());

    return new ProductionMatchTrackingService(productionMatchRepository, productionNamingStrategy);
  }
}
