package com.silenteight.hsbc.bridge.bulk;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.alert.AlertFacade;
import com.silenteight.hsbc.bridge.analysis.AnalysisFacade;
import com.silenteight.hsbc.bridge.recommendation.RecommendationFacade;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class BulkUseCaseConfiguration {

  private final AnalysisFacade analysisFacade;
  private final BulkRepository bulkRepository;
  private final RecommendationFacade recommendationFacade;

  @Bean
  StoreBulkUseCase storeBulkUseCase() {
    return new StoreBulkUseCase(bulkIdRetriever(), bulkRepository);
  }

  @Bean
  GetBulkStatusUseCase getBulkStatusUseCase() {
    return new GetBulkStatusUseCase(analysisFacade, bulkRepository);
  }

  @Bean
  GetBulkResultsUseCase getBulkResultsUseCase() {
    return new GetBulkResultsUseCase(bulkRepository, recommendationFacade);
  }

  @Bean
  AcknowledgeBulkDeliveryUseCase acknowledgeBulkDeliveryUseCase() {
    return new AcknowledgeBulkDeliveryUseCase(bulkRepository);
  }

  @Bean
  CancelBulkUseCase cancelBulkUseCase() {
    return new CancelBulkUseCase(bulkRepository);
  }

  private BulkIdRetriever bulkIdRetriever() {
    return new BulkIdRetriever();
  }
}
