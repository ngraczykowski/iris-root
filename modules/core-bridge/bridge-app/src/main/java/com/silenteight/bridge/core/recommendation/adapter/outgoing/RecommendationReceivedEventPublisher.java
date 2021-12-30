package com.silenteight.bridge.core.recommendation.adapter.outgoing;

import lombok.RequiredArgsConstructor;

import com.silenteight.bridge.core.recommendation.domain.model.RecommendationsReceivedEvent;
import com.silenteight.bridge.core.recommendation.domain.port.outgoing.RecommendationEventPublisher;
import com.silenteight.bridge.core.recommendation.infrastructure.amqp.RecommendationRabbitProperties;
import com.silenteight.proto.recommendation.api.v1.RecommendationsReceived;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class RecommendationReceivedEventPublisher implements RecommendationEventPublisher {

  private final RabbitTemplate rabbitTemplate;
  private final RecommendationRabbitProperties recommendationOutgoingProperties;

  @Override
  public void publish(RecommendationsReceivedEvent event) {
    var message = RecommendationsReceived.newBuilder()
        .setAnalysisId(event.analysisName())
        .addAllAlertIds(event.alertNames())
        .build();

    rabbitTemplate.convertAndSend(recommendationOutgoingProperties.exchangeName(), "", message);
  }
}
