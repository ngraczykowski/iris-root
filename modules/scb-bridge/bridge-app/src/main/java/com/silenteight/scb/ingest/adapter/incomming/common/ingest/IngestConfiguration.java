package com.silenteight.scb.ingest.adapter.incomming.common.ingest;

import lombok.RequiredArgsConstructor;

import com.silenteight.scb.ingest.adapter.incomming.common.recommendation.ScbRecommendationService;
import com.silenteight.scb.ingest.domain.AlertRegistrationFacade;
import com.silenteight.scb.ingest.domain.port.outgoing.IngestEventPublisher;
import com.silenteight.sep.base.common.messaging.MessagingConfiguration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
@Import(MessagingConfiguration.class)
class IngestConfiguration {

  private final AlertRegistrationFacade alertRegistrationFacade;
  private final IngestEventPublisher ingestEventPublisher;

  @Bean
  IngestService ingestService(
      ScbRecommendationService scbRecommendationService) {

    return IngestService.builder()
        .scbRecommendationService(scbRecommendationService)
        .alertRegistrationFacade(alertRegistrationFacade)
        .ingestEventPublisher(ingestEventPublisher)
        .build();
  }

  @Bean
  @ConditionalOnBean(IngestService.class)
  IngestServiceMetrics ingestServiceMetrics(IngestService ingestService) {
    return new IngestServiceMetrics(ingestService);
  }
}
