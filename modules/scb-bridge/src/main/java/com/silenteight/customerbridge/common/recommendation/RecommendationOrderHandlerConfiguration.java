package com.silenteight.customerbridge.common.recommendation;

import lombok.RequiredArgsConstructor;

import com.silenteight.customerbridge.common.batch.SingleAlertFetcher;
import com.silenteight.customerbridge.common.ingest.SingleAlertIngestService;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

import static java.time.Duration.ofMillis;

@Configuration
@RequiredArgsConstructor
class RecommendationOrderHandlerConfiguration {

  private final RecommendationOrderProperties recommendationOrderProperties;

  @Bean
  RecommendationOrderHandler recommendationOrderService(
      @Qualifier("recommendationAlertFetcher") SingleAlertFetcher alertFetcher,
      SingleAlertIngestService ingestService) {
    return new RecommendationOrderHandler(alertFetcher, ingestService, getTimeout());
  }

  private Duration getTimeout() {
    return ofMillis(recommendationOrderProperties.getTimeout());
  }
}
