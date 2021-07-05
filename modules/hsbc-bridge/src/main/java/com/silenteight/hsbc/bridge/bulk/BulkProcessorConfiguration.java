package com.silenteight.hsbc.bridge.bulk;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.adjudication.AdjudicationFacade;
import com.silenteight.hsbc.bridge.alert.AlertSender;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class BulkProcessorConfiguration {

  private final AdjudicationFacade adjudicationFacade;
  private final BulkRepository bulkRepository;
  private final AlertSender alertSender;

  @Bean
  BulkProcessor bulkProcessor() {
    return new BulkProcessor(adjudicationFacade, alertSender, bulkRepository);
  }
}
