package com.silenteight.bridge.core.recommendation.adapter.incoming;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v1.RecommendationsGenerated;
import com.silenteight.bridge.core.recommendation.domain.RecommendationFacade;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class RecommendationsRabbitAmqpListener {

  private final RecommendationFacade recommendationFacade;

  @RabbitListener(queues = "${amqp.recommendation.incoming.recommendations-generated.queue-name}")
  public void subscribe(RecommendationsGenerated recommendation) {
    log.info(
        "Received RecommendationsGenerated amqp message for analysis={}",
        recommendation.getAnalysis());
    recommendationFacade.proceedReadyRecommendations(recommendation.getAnalysis());
  }
}
