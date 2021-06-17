package com.silenteight.hsbc.bridge.recommendation;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.alert.event.AlertRecommendationReadyEvent;
import com.silenteight.hsbc.bridge.analysis.dto.GetRecommendationsDto;
import com.silenteight.hsbc.bridge.analysis.event.AnalysisCompletedEvent;
import com.silenteight.hsbc.bridge.recommendation.RecommendationServiceClient.CannotGetRecommendationsException;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
class RecommendationHandler {

  private final RecommendationRepository repository;
  private final RecommendationServiceClient recommendationServiceClient;
  private final ApplicationEventPublisher eventPublisher;

  @Transactional
  public void getAndStoreRecommendations(@NonNull String analysis, String dataset) {
    var request = GetRecommendationsDto.builder()
        .analysis(analysis)
        .dataset(dataset)
        .build();

    log.info("NOMAD, Ask for recommendations, request:{}", request);

    try {
      var recommendations = recommendationServiceClient.getRecommendations(request);
      recommendations.forEach(this::handleRecommendation);

      eventPublisher.publishEvent(new AnalysisCompletedEvent(analysis));
    } catch (CannotGetRecommendationsException ex) {
      log.error("Cannot get recommendation for analysis={}, dataset={}", analysis, dataset);
    }
  }

  private void handleRecommendation(RecommendationDto recommendation) {
    var alert = recommendation.getAlert();

    if (doesNotExist(recommendation.getName())) {
      save(recommendation);

      log.debug("Recommendation has been stored, alert={}", alert);

      eventPublisher.publishEvent(new AlertRecommendationReadyEvent(alert));
    }
  }

  private void save(RecommendationDto recommendation) {
    repository.save(new RecommendationEntity(recommendation));
  }

  private boolean doesNotExist(String name) {
    return !repository.existsByName(name);
  }
}
