package com.silenteight.warehouse.indexer.indextracking;

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

    ProductionNamingStrategy productionNamingStrategy =
        new ProductionNamingStrategy(timeSource, environmentProperties.getPrefix());

    return new ProductionAlertTrackingService(productionAlertRepository, productionNamingStrategy);
  }

  @Bean
  IndexByDiscriminatorResolverFactory productionWriteIndexProviderFactory(
      ProductionAlertTrackingService productionAlertTrackingService) {

    return new IndexByDiscriminatorResolverFactory(productionAlertTrackingService);
  }
}
