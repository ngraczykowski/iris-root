package com.silenteight.bridge.core.recommendation.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.bridge.core.recommendation.domain.command.ProceedBatchTimeoutCommand;
import com.silenteight.bridge.core.recommendation.domain.command.ProceedReadyRecommendationsCommand;
import com.silenteight.bridge.core.recommendation.domain.model.RecommendationMetadata;
import com.silenteight.bridge.core.recommendation.domain.model.RecommendationWithMetadata;
import com.silenteight.bridge.core.recommendation.domain.model.RecommendationsStoredEvent;
import com.silenteight.bridge.core.recommendation.domain.model.RecommendedAction;
import com.silenteight.bridge.core.recommendation.domain.port.outgoing.RecommendationEventPublisher;
import com.silenteight.bridge.core.recommendation.domain.port.outgoing.RecommendationRepository;

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
  private static final RecommendationMetadata TIMED_OUT_RECOMMENDATION_METADATA =
      new RecommendationMetadata(List.of());

  private final RecommendationEventPublisher eventPublisher;
  private final RecommendationRepository recommendationRepository;

  void proceedReadyRecommendations(
      ProceedReadyRecommendationsCommand command,
      Integer priority) {
    var newRecommendations =
        filterOutExistingInDb(command.analysisName(), command.recommendationsWithMetadata());

    if (CollectionUtils.isEmpty(newRecommendations)) {
      log.info("No new recommendations to process for analysis name: [{}]. "
          + "Storing in DB and event publishing skipped", command.analysisName());
      return;
    }

    log.info(
        "Received [{}] new recommendations from [{}] sent for analysis [{}].",
        newRecommendations.size(),
        command.recommendationsWithMetadata().size(),
        command.analysisName());

    saveRecommendations(command.analysisName(), newRecommendations);
    eventPublisher.publish(new RecommendationsStoredEvent(
        command.analysisName(),
        getAlertNames(newRecommendations),
        false,
        priority));
  }

  void createTimedOutRecommendations(ProceedBatchTimeoutCommand command, Integer priority) {
    var createdRecommendations = command.alertNames().stream()
        .map(alertName -> RecommendationWithMetadata.builder()
            .name(TIMED_OUT_RECOMMENDATION_NAME)
            .alertName(alertName)
            .analysisName(command.analysisName())
            .recommendedAction(RecommendedAction.ACTION_INVESTIGATE.name())
            .recommendationComment(TIMED_OUT_RECOMMENDATION_COMMENT)
            .recommendedAt(OffsetDateTime.now())
            .metadata(TIMED_OUT_RECOMMENDATION_METADATA)
            .timeout(RECOMMENDATION_IS_TIMED_OUT)
            .build())
        .toList();

    var newRecommendations = filterOutExistingInDb(
        command.analysisName(), createdRecommendations);

    if (CollectionUtils.isEmpty(newRecommendations)) {
      log.info("No new timed out recommendations to process for analysis name: [{}]. "
          + "Storing in DB and event publishing skipped", command.analysisName());
      return;
    }

    saveRecommendations(command.analysisName(), newRecommendations);
    eventPublisher.publish(new RecommendationsStoredEvent(
        command.analysisName(),
        getAlertNames(newRecommendations),
        true,
        priority));
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

  private void saveRecommendations(
      String analysisName, List<RecommendationWithMetadata> recommendations) {
    recommendationRepository.saveAll(recommendations);
    log.info(
        "[{}] recommendations saved in DB for analysis [{}]",
        recommendations.size(),
        analysisName);
  }

  private List<String> getAlertNames(List<RecommendationWithMetadata> recommendation) {
    return recommendation.stream()
        .map(RecommendationWithMetadata::alertName)
        .toList();
  }
}
