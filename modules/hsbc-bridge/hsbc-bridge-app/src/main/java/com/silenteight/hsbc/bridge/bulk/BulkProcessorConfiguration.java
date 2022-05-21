package com.silenteight.hsbc.bridge.bulk;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.adjudication.AdjudicationFacade;
import com.silenteight.hsbc.bridge.alert.AlertFacade;
import com.silenteight.hsbc.bridge.alert.LearningAlertProcessor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class BulkProcessorConfiguration {

  private final AdjudicationFacade adjudicationFacade;
  private final AlertFacade alertFacade;
  private final BulkRepository bulkRepository;
  private final LearningAlertProcessor learningAlertProcessor;

  @Bean
  BulkProcessor bulkProcessor() {
    return new BulkProcessor(
        adjudicationFacade,
        alertFacade,
        learningAlertProcessor,
        bulkRepository);
  }
}
