package com.silenteight.hsbc.bridge.amqp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v1.RecommendationsGenerated;
import com.silenteight.adjudication.api.v1.RecommendationsGenerated.RecommendationInfo;
import com.silenteight.hsbc.bridge.recommendation.event.AlertRecommendationInfo;
import com.silenteight.hsbc.bridge.recommendation.event.RecommendationsGeneratedEvent;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Slf4j
class RecommendationGeneratedListener {

  private final ApplicationEventPublisher eventPublisher;

  @RabbitListener(queues = "${silenteight.bridge.amqp.ingoing.recommendations-queue}")
  void onRecommendation(RecommendationsGenerated recommendation) {
    log.info("Received RecommendationsGenerated for analysis={}", recommendation.getAnalysis());

    eventPublisher.publishEvent(RecommendationsGeneratedEvent.builder()
            .analysis(recommendation.getAnalysis())
            .alertRecommendationInfos(map(recommendation.getRecommendationInfosList()))
            .build());
  }

  private List<AlertRecommendationInfo> map(List<RecommendationInfo> list) {
    return list.stream()
        .map(a-> new AlertRecommendationInfo(a.getAlert(), a.getRecommendation()))
        .collect(toList());
  }
}
