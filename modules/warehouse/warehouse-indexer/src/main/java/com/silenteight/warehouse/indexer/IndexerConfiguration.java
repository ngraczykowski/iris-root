package com.silenteight.warehouse.indexer;

import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.warehouse.indexer.alert.AlertService;
import com.silenteight.warehouse.indexer.analysis.UniqueAnalysisFactory;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
    IndexerIntegrationProperties.class
})
class IndexerConfiguration {

  @Bean
  AlertIndexUseCase alertIndexUseCase(
      AlertService alertService,
      UniqueAnalysisFactory uniqueAnalysisFactory,
      TimeSource timeSource) {
    return new AlertIndexUseCase(alertService, uniqueAnalysisFactory, timeSource);
  }
}
