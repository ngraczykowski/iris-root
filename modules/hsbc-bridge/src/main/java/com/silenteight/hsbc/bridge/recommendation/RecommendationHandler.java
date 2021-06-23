package com.silenteight.hsbc.bridge.recommendation;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.analysis.dto.GetRecommendationsDto;
import com.silenteight.hsbc.bridge.recommendation.RecommendationServiceClient.CannotGetRecommendationsException;
import com.silenteight.hsbc.bridge.recommendation.event.AlertRecommendationsStoredEvent;
import com.silenteight.hsbc.bridge.recommendation.event.FailedToGetRecommendationsEvent;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Optional.*;
import static java.util.Optional.of;

@Slf4j
@RequiredArgsConstructor
class RecommendationHandler {

  private final RecommendationRepository repository;
  private final RecommendationServiceClient recommendationServiceClient;
  private final ApplicationEventPublisher eventPublisher;

  @Transactional
  public void getAndStoreRecommendations(@NonNull String analysis) {
    tryToGetRecommendations(analysis).ifPresent(recommendations -> {
      storeRecommendations(recommendations);

      var alerts = recommendations.stream()
          .map(RecommendationDto::getAlert)
          .collect(Collectors.toList());
      notifyAboutStoredRecommendations(analysis, alerts);
    });
  }

  private void storeRecommendations(Collection<RecommendationDto> recommendations) {
    recommendations.forEach(r -> {
      var name = r.getName();

      if (doesNotExist(name)) {
        save(r);

        log.debug("Recommendation stored, alert={}, recommendation={}", r.getAlert(), name);
      }
    });
  }

  private Optional<Collection<RecommendationDto>> tryToGetRecommendations(String analysis) {
    try {
      var request = new GetRecommendationsDto(analysis);
      return of(recommendationServiceClient.getRecommendations(request));
    } catch (CannotGetRecommendationsException ex) {
      log.error("Cannot get recommendation for analysis={}", analysis);
      eventPublisher.publishEvent(new FailedToGetRecommendationsEvent(analysis));
      return empty();
    }
  }

  private void notifyAboutStoredRecommendations(String analysis, List<String> alerts) {
    eventPublisher.publishEvent(new AlertRecommendationsStoredEvent(analysis, alerts));
  }

  private void save(RecommendationDto recommendation) {
    repository.save(new RecommendationEntity(recommendation));
  }

  private boolean doesNotExist(String name) {
    return !repository.existsByName(name);
  }
}
