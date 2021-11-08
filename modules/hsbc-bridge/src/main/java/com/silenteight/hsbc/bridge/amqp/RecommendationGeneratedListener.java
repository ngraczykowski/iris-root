package com.silenteight.hsbc.bridge.amqp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v1.RecommendationsGenerated;
import com.silenteight.adjudication.api.v1.RecommendationsGenerated.RecommendationInfo;
import com.silenteight.hsbc.bridge.recommendation.event.AlertRecommendationInfo;
import com.silenteight.hsbc.bridge.recommendation.event.RecommendationsGeneratedEvent;

import io.micrometer.core.annotation.Timed;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
class RecommendationGeneratedListener {

  private final ApplicationEventPublisher eventPublisher;

  @RabbitListener(queues = "${silenteight.bridge.amqp.ingoing.recommendations-queue}")
  @Timed(histogram = true)
  void onRecommendation(RecommendationsGenerated recommendation) {
    log.debug("Received RecommendationsGenerated amqp message for analysis={}", recommendation.getAnalysis());

    eventPublisher.publishEvent(RecommendationsGeneratedEvent.builder()
        .analysis(recommendation.getAnalysis())
        .alertRecommendationInfos(map(recommendation.getRecommendationInfosList()))
        .build());
  }

  private List<AlertRecommendationInfo> map(List<RecommendationInfo> list) {
    return list.stream()
        .map(a -> new AlertRecommendationInfo(a.getAlert(), a.getRecommendation()))
        .collect(Collectors.toList());
  }
}
