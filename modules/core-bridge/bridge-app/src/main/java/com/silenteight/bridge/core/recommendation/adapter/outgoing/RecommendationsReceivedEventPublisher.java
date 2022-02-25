package com.silenteight.bridge.core.recommendation.adapter.outgoing;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.bridge.core.recommendation.domain.model.RecommendationsReceivedEvent;
import com.silenteight.bridge.core.recommendation.domain.port.outgoing.RecommendationEventPublisher;
import com.silenteight.bridge.core.recommendation.infrastructure.amqp.RecommendationOutgoingRecommendationsReceivedConfigurationProperties;
import com.silenteight.proto.recommendation.api.v1.RecommendationsReceived;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class RecommendationsReceivedEventPublisher implements RecommendationEventPublisher {

  private final RabbitTemplate rabbitTemplate;
  private final RecommendationOutgoingRecommendationsReceivedConfigurationProperties properties;

  @Override
  public void publish(RecommendationsReceivedEvent event) {
    var message = RecommendationsReceived.newBuilder()
        .setAnalysisId(event.analysisName())
        .addAllAlertIds(event.alertNames())
        .build();

    log.info(
        "Send RecommendationsReceivedEvent with {} alerts for analysis name: {}",
        event.alertNames().size(),
        event.analysisName());
    rabbitTemplate.convertAndSend(properties.exchangeName(), "", message);
  }
}
