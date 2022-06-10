package com.silenteight.scb.ingest.adapter.incomming.common.ingest;

import lombok.RequiredArgsConstructor;

import com.silenteight.scb.ingest.adapter.incomming.common.recommendation.ScbRecommendationService;
import com.silenteight.scb.ingest.adapter.incomming.common.store.batchinfo.BatchInfoService;
import com.silenteight.scb.ingest.adapter.incomming.common.trafficmanagement.TrafficManager;
import com.silenteight.scb.ingest.domain.AlertRegistrationFacade;
import com.silenteight.scb.reports.domain.port.outgoing.ReportsSenderService;
import com.silenteight.sep.base.common.messaging.MessagingConfiguration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
@Import(MessagingConfiguration.class)
class IngestConfiguration {

  private final ScbRecommendationService scbRecommendationService;
  private final AlertRegistrationFacade alertRegistrationFacade;
  private final UdsFeedingPublisher udsFeedingPublisher;
  private final ReportsSenderService reportsSenderService;
  private final TrafficManager trafficManager;
  private final BatchInfoService batchInfoService;
  private final IngestedLearningAlertsCounter learningAlertsCounter;

  @Bean
  IngestService ingestService() {
    return IngestService.builder()
        .scbRecommendationService(scbRecommendationService)
        .alertRegistrationFacade(alertRegistrationFacade)
        .udsFeedingPublisher(udsFeedingPublisher)
        .reportsSenderService(reportsSenderService)
        .trafficManager(trafficManager)
        .batchInfoService(batchInfoService)
        .learningAlertsCounter(learningAlertsCounter)
        .build();
  }
}
