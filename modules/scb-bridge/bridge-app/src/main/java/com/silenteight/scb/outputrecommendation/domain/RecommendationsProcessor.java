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

  private final QcoRecommendationService qcoRecommendationService;
  private final RecommendationsMapper recommendationsMapper;
  private final RecommendationApiClient recommendationApiClient;
  private final RecommendationPublisher recommendationPublisher;
  private final RecommendationDeliveredEventPublisher recommendationDeliveredEventPublisher;

  void processBatchCompleted(PrepareRecommendationResponseCommand command) {

    // Note, when alertNames is empty, it means to get all alerts within analysis with analysisName
    var recommendations =
        recommendationApiClient.getRecommendations(command.analysisName(), EMPTY_ALERT_NAMES);

    var recommendationsEvent =
        recommendationsMapper.toBatchCompletedRecommendationsEvent(command, recommendations);

    var recommendationsDeliveredEvent =
        recommendationsMapper.toRecommendationsDeliveredEvent(
            command.batchId(), command.analysisName());

    var qcoUpdatedRecommendations = qcoRecommendationService.process(recommendationsEvent);
    recommendationPublisher.publishCompleted(qcoUpdatedRecommendations);
    recommendationDeliveredEventPublisher.publish(recommendationsDeliveredEvent);
  }

  void processBatchError(PrepareErrorRecommendationResponseCommand command) {
    var errorRecommendationsEvent =
        recommendationsMapper.toBatchErrorRecommendationsEvent(command);

    var recommendationsDeliveredEvent =
        recommendationsMapper.toRecommendationsDeliveredEvent(
            command.batchId(), EMPTY_ANALYSIS_NAME);

    recommendationPublisher.publishError(errorRecommendationsEvent);
    recommendationDeliveredEventPublisher.publish(recommendationsDeliveredEvent);
  }
}
