package com.silenteight.hsbc.bridge.bulk;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.alert.AlertFacade;
import com.silenteight.hsbc.bridge.recommendation.GetRecommendationUseCase;
import com.silenteight.hsbc.bridge.report.WarehouseFacade;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class BulkUseCaseConfiguration {

  private final AlertFacade alertFacade;
  private final ApplicationEventPublisher eventPublisher;
  private final WarehouseFacade warehouseFacade;
  private final BulkRepository bulkRepository;
  private final GetRecommendationUseCase getRecommendationUseCase;

  @Bean
  StoreBulkUseCase storeBulkUseCase() {
    return new StoreBulkUseCase(alertFacade, bulkRepository, eventPublisher);
  }

  @Bean
  GetBulkStatusUseCase getBulkStatusUseCase() {
    return new GetBulkStatusUseCase(bulkRepository);
  }

  @Bean
  GetBulkResultsUseCase getBulkResultsUseCase() {
    return new GetBulkResultsUseCase(bulkRepository, getRecommendationUseCase);
  }

  @Bean
  AcknowledgeBulkDeliveryUseCase acknowledgeBulkDeliveryUseCase() {
    return new AcknowledgeBulkDeliveryUseCase(bulkRepository);
  }

  @Bean
  CancelBulkUseCase cancelBulkUseCase() {
    return new CancelBulkUseCase(bulkRepository);
  }

  @Bean
  IngestRecommendationsUseCase ingestRecommendationsUseCase() {
    return new IngestRecommendationsUseCase(warehouseFacade);
  }
}
