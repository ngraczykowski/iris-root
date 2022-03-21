package com.silenteight.bridge.core.recommendation.adapter.incoming;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v1.RecommendationsGenerated;
import com.silenteight.bridge.core.recommendation.domain.RecommendationFacade;
import com.silenteight.bridge.core.recommendation.domain.command.ProceedReadyRecommendationsCommand;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class RecommendationsRabbitAmqpListener {

  private final RecommendationFacade recommendationFacade;
  private final RecommendationsMapper mapper;

  @RabbitListener(
      queues = "${amqp.recommendation.incoming.recommendations-generated.queue-name}",
      errorHandler = "recommendationAmqpErrorHandler"
  )
  public void subscribe(RecommendationsGenerated recommendations) {
    var analysisName = recommendations.getAnalysis();
    log.info(
        "Received RecommendationsGenerated amqp message for analysis={} with {} recommendations.",
        analysisName, recommendations.getRecommendationInfosList().size());

    var recommendationsWithMetadata = recommendations.getRecommendationInfosList().stream()
        .map(recommendationInfo ->
            mapper.toRecommendationWithMetadata(recommendationInfo, analysisName))
        .toList();

    recommendationFacade.proceedReadyRecommendations(
        new ProceedReadyRecommendationsCommand(recommendationsWithMetadata));
  }
}
