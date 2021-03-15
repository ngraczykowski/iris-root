package com.silenteight.hsbc.bridge.bulk;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.alert.AlertFacade;
import com.silenteight.hsbc.bridge.bulk.repository.BulkRepository;
import com.silenteight.hsbc.bridge.match.MatchFacade;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class BulkUseCaseConfiguration {

  private final AlertFacade alertFacade;
  private final MatchFacade matchFacade;
  private final BulkRepository bulkRepository;
  private final ApplicationEventPublisher eventPublisher;

  @Bean
  CreateBulkUseCase createBulkUseCase() {
    return new CreateBulkUseCase(new BulkItemPayloadConverter(), bulkRepository, eventPublisher);
  }

  @Bean
  GetBulkStatusUseCase getBulkStatusUseCase() {
    return new GetBulkStatusUseCase(bulkRepository);
  }

  @Bean
  GetBulkResultsUseCase getBulkResultsUseCase() {
    return new GetBulkResultsUseCase(bulkRepository);
  }

  @Bean
  CancelBulkUseCase cancelBulkUseCase() {
    return new CancelBulkUseCase(bulkRepository);
  }

  @Bean
  BulkProcessor bulkProcessor() {
    return new BulkProcessor(alertFacade, bulkRepository, eventPublisher, matchFacade);
  }
}
