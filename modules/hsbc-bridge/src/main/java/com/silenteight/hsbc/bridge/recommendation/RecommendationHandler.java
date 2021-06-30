package com.silenteight.hsbc.bridge.recommendation;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.recommendation.RecommendationServiceClient.CannotGetRecommendationsException;
import com.silenteight.hsbc.bridge.recommendation.event.AlertRecommendationsStoredEvent;
import com.silenteight.hsbc.bridge.recommendation.event.FailedToGetRecommendationsEvent;

import org.springframework.context.ApplicationEventPublisher;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Optional.*;
import static java.util.Optional.of;

@Slf4j
@RequiredArgsConstructor
class RecommendationHandler {

  private final StoreRecommendationsUseCase storeRecommendationsUseCase;
  private final RecommendationServiceClient recommendationServiceClient;
  private final ApplicationEventPublisher eventPublisher;

  void getAndStoreRecommendations(@NonNull String analysis) {
    log.info("Get and store recommendations for analysis={}", analysis);

    tryToGetRecommendations(analysis).ifPresent(recommendations -> {
      storeRecommendationsUseCase.store(recommendations);

      var alerts = recommendations.stream()
          .map(RecommendationWithMetadataDto::getAlert)
          .collect(Collectors.toList());
      notifyAboutStoredRecommendations(analysis, alerts);
    });
  }

  private Optional<Collection<RecommendationWithMetadataDto>> tryToGetRecommendations(String analysis) {
    try {
      return of(recommendationServiceClient.getRecommendations(analysis));
    } catch (CannotGetRecommendationsException ex) {
      log.error("Cannot get recommendation for analysis={}", analysis);
      eventPublisher.publishEvent(new FailedToGetRecommendationsEvent(analysis));
      return empty();
    }
  }

  private void notifyAboutStoredRecommendations(String analysis, List<String> alerts) {
    eventPublisher.publishEvent(new AlertRecommendationsStoredEvent(analysis, alerts));
  }
}
