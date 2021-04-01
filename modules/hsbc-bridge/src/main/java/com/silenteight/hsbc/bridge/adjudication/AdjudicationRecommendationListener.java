package com.silenteight.hsbc.bridge.adjudication;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.Recommendation;
import com.silenteight.hsbc.bridge.common.util.TimestampUtil;
import com.silenteight.hsbc.bridge.recommendation.RecommendationDto;
import com.silenteight.hsbc.bridge.recommendation.event.NewRecommendationEvent;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.ApplicationEventPublisher;

@RequiredArgsConstructor
class AdjudicationRecommendationListener {

  private final ApplicationEventPublisher applicationEventPublisher;

  @RabbitListener(queues = "${silenteight.bridge.recommendations.queue}")
  void onRecommendation(Recommendation recommendation) {
    applicationEventPublisher.publishEvent(toApplicationEvent(recommendation));
  }

  private static NewRecommendationEvent toApplicationEvent(Recommendation recommendation) {
    return new NewRecommendationEvent(toRecommendationDto(recommendation));
  }

  private static RecommendationDto toRecommendationDto(Recommendation recommendation) {
    return RecommendationDto.builder()
        .alert(recommendation.getAlert())
        .name(recommendation.getName())
        .date(TimestampUtil.toOffsetDateTime(recommendation.getCreateTime()))
        .recommendationComment(recommendation.getRecommendationComment())
        .recommendedAction(recommendation.getRecommendedAction())
        .build();
  }
}
