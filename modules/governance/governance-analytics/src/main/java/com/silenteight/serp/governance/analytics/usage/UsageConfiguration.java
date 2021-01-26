package com.silenteight.serp.governance.analytics.usage;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan
@EnableJpaRepositories
class UsageConfiguration {

  @Bean
  UsageService usageService(
      AnalyticsFeatureVectorUsageRepository analyticsFeatureVectorUsageRepository) {
    return new UsageService(analyticsFeatureVectorUsageRepository);
  }
}
