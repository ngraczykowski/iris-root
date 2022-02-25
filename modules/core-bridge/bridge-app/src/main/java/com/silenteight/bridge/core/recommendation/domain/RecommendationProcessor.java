package com.silenteight.bridge.core.recommendation.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.bridge.core.recommendation.domain.exception.CannotGetRecommendationsException;
import com.silenteight.bridge.core.recommendation.domain.model.RecommendationMetadata;
import com.silenteight.bridge.core.recommendation.domain.model.RecommendationWithMetadata;
import com.silenteight.bridge.core.recommendation.domain.model.RecommendationsReceivedEvent;
import com.silenteight.bridge.core.recommendation.domain.model.RecommendedAction;
import com.silenteight.bridge.core.recommendation.domain.port.outgoing.RecommendationEventPublisher;
import com.silenteight.bridge.core.recommendation.domain.port.outgoing.RecommendationRepository;
import com.silenteight.bridge.core.recommendation.domain.port.outgoing.RecommendationService;

import io.vavr.control.Try;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
class RecommendationProcessor {

  private static final String TIMED_OUT_RECOMMENDATION_NAME = "";
  private static final boolean RECOMMENDATION_IS_TIMED_OUT = true;
  private static final String TIMED_OUT_RECOMMENDATION_COMMENT = "";
  private static final RecommendationMetadata TIMED_OUT_RECOMMENDATION_METADATA = null;

  private final RecommendationService recommendationService;
  private final RecommendationEventPublisher eventPublisher;
  private final RecommendationRepository recommendationRepository;

  void proceedReadyRecommendations(String analysisName) {
    var receivedRecommendations = getRecommendations(analysisName);
    var newRecommendations = filterOutExistingInDb(analysisName, receivedRecommendations);

    log.info(
        "Received {} new recommendations from {} sent.",
        newRecommendations.size(),
        receivedRecommendations.size());

    saveRecommendations(newRecommendations);
    publishRecommendationsReceivedEvent(analysisName, newRecommendations);
  }

  void createTimedOutRecommendations(String analysisName, List<String> alertNames) {
    var createdRecommendations = alertNames.stream()
        .map(alertName -> RecommendationWithMetadata.builder()
            .name(TIMED_OUT_RECOMMENDATION_NAME)
            .alertName(alertName)
            .analysisName(analysisName)
            .recommendedAction(RecommendedAction.ACTION_INVESTIGATE.name())
            .recommendationComment(TIMED_OUT_RECOMMENDATION_COMMENT)
            .recommendedAt(OffsetDateTime.now())
            .metadata(TIMED_OUT_RECOMMENDATION_METADATA)
            .timeout(RECOMMENDATION_IS_TIMED_OUT)
            .build())
        .toList();

    var newRecommendations = filterOutExistingInDb(analysisName, createdRecommendations);

    saveRecommendations(newRecommendations);
    publishRecommendationsReceivedEvent(analysisName, newRecommendations);
  }

  private List<RecommendationWithMetadata> getRecommendations(String analysisName) {
    return Try.of(() -> recommendationService.getRecommendations(analysisName))
        .onSuccess(
            r -> log.info("{} recommendations received for analysis={}.", r.size(), analysisName))
        .onFailure(e -> log.error("Cannot get recommendations for analysis={}", analysisName, e))
        .getOrElseThrow(CannotGetRecommendationsException::new);
  }

  private List<RecommendationWithMetadata> filterOutExistingInDb(
      String analysisName, List<RecommendationWithMetadata> recommendations) {

    var existingRecommendationAlertNames =
        recommendationRepository.findRecommendationAlertNamesByAnalysisName(analysisName);

    if (CollectionUtils.isNotEmpty(existingRecommendationAlertNames)) {
      return recommendations.stream()
          .filter(recommendation -> existingRecommendationAlertNames.stream()
              .noneMatch(existing -> recommendation.alertName().equals(existing)))
          .toList();
    }
    return recommendations;
  }

  private void saveRecommendations(List<RecommendationWithMetadata> recommendations) {
    recommendationRepository.saveAll(recommendations);
    log.info("{} recommendations saved in DB.", recommendations.size());
  }

  private void publishRecommendationsReceivedEvent(
      String analysisName, List<RecommendationWithMetadata> recommendationsWithMetadata) {

    var alertNames = recommendationsWithMetadata.stream()
        .map(RecommendationWithMetadata::alertName)
        .toList();

    eventPublisher.publish(new RecommendationsReceivedEvent(analysisName, alertNames));
  }
}
