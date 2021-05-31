package com.silenteight.hsbc.bridge.amqp;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.RecommendationsGenerated;
import com.silenteight.hsbc.bridge.recommendation.event.NewRecommendationsEvent;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.ApplicationEventPublisher;

@RequiredArgsConstructor
class RecommendationGeneratedListener {

  private final ApplicationEventPublisher eventPublisher;

  @RabbitListener(queues = "${silenteight.bridge.recommendations.queue}")
  void onRecommendation(RecommendationsGenerated recommendation) {
    eventPublisher.publishEvent(NewRecommendationsEvent.builder()
            .analysis(recommendation.getAnalysis())
            .build());
  }
}
