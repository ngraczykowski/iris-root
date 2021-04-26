package com.silenteight.hsbc.bridge.adjudication;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.RecommendationsGenerated;
import com.silenteight.hsbc.bridge.analysis.AnalysisServiceApi;
import com.silenteight.hsbc.bridge.analysis.dto.GetRecommendationsDto;
import com.silenteight.hsbc.bridge.recommendation.event.NewRecommendationEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.ApplicationEventPublisher;

@RequiredArgsConstructor
class AdjudicationRecommendationListener {

  private final ApplicationEventPublisher applicationEventPublisher;
  private final AnalysisServiceApi analysisServiceApi;

  @RabbitListener(queues = "${silenteight.bridge.recommendations.queue}")
  void onRecommendation(RecommendationsGenerated recommendation) {
    var request = GetRecommendationsDto.builder().analysis(recommendation.getAnalysis()).build();

    analysisServiceApi.getRecommendations(request).stream()
            .map(NewRecommendationEvent::new)
            .forEach(applicationEventPublisher::publishEvent);
  }
}
