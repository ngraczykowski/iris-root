package com.silenteight.hsbc.bridge.bulk;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.adjudication.AdjudicationFacade;
import com.silenteight.hsbc.bridge.alert.AlertFacade;
import com.silenteight.hsbc.bridge.match.MatchFacade;
import com.silenteight.hsbc.bridge.report.WarehouseClient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class BulkProcessorConfiguration {

  private final AlertFacade alertFacade;
  private final AdjudicationFacade adjudicationFacade;
  private final BulkRepository bulkRepository;
  private final MatchFacade matchFacade;
  private final WarehouseClient warehouseClient;

  @Bean
  BulkPreProcessor bulkPreProcessor() {
    return new BulkPreProcessor(alertFacade, bulkRepository, matchFacade);
  }

  @Bean
  BulkProcessor bulkProcessor() {
    return new BulkProcessor(adjudicationFacade, bulkRepository, warehouseClient);
  }
}
