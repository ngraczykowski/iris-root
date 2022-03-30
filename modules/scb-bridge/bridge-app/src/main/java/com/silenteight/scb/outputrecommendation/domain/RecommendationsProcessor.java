package com.silenteight.scb.outputrecommendation.domain;

import lombok.RequiredArgsConstructor;

import com.silenteight.scb.outputrecommendation.domain.port.outgoing.RecommendationApiClient;
import com.silenteight.scb.outputrecommendation.domain.port.outgoing.RecommendationDeliveredEventPublisher;
import com.silenteight.scb.outputrecommendation.domain.port.outgoing.RecommendationPublisher;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
class RecommendationsProcessor {

  private static final String EMPTY_ANALYSIS_NAME = "";

  private static final List<String> EMPTY_ALERT_NAMES = List.of();

  private final RecommendationsMapper recommendationsMapper;
  private final RecommendationApiClient recommendationApiClient;
  private final RecommendationPublisher recommendationPublisher;
  private final RecommendationDeliveredEventPublisher recommendationDeliveredEventPublisher;

  void processBatchCompleted(PrepareRecommendationResponseCommand command) {
    var recommendations =
        recommendationApiClient.getRecommendations(command.analysisName(), command.alertNames());

    var recommendationsEvent =
        recommendationsMapper.toBatchCompletedRecommendationsEvent(command, recommendations);

    var recommendationsDeliveredEvent =
        recommendationsMapper.toRecommendationsDeliveredEvent(
            command.batchId(), command.analysisName(), command.alertNames());

    recommendationPublisher.publishCompleted(recommendationsEvent);
    recommendationDeliveredEventPublisher.publish(recommendationsDeliveredEvent);
  }

  void processBatchError(PrepareErrorRecommendationResponseCommand command) {
    var errorRecommendationsEvent =
        recommendationsMapper.toBatchErrorRecommendationsEvent(command);

    var recommendationsDeliveredEvent =
        recommendationsMapper.toRecommendationsDeliveredEvent(
            command.batchId(), EMPTY_ANALYSIS_NAME, EMPTY_ALERT_NAMES);

    recommendationPublisher.publishError(errorRecommendationsEvent);
    recommendationDeliveredEventPublisher.publish(recommendationsDeliveredEvent);
  }
}
