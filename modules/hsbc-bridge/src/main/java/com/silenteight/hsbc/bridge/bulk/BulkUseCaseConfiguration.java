package com.silenteight.hsbc.bridge.bulk;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.alert.AlertFacade;
import com.silenteight.hsbc.bridge.bulk.event.BulkStoredEventListener;
import com.silenteight.hsbc.bridge.bulk.repository.BulkRepository;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class BulkUseCaseConfiguration {

  private final AlertFacade alertFacade;
  private final BulkRepository bulkRepository;
  private final ApplicationEventPublisher eventPublisher;

  @Bean
  CreateBulkUseCase createBulkUseCase() {
    return new CreateBulkUseCase(alertFacade, bulkRepository, eventPublisher);
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
  BulkStoredEventListener bulkStoredEventListener() {
    return new BulkStoredEventListener();
  }
}
