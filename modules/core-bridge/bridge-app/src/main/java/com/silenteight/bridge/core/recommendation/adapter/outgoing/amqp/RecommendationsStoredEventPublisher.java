package com.silenteight.bridge.core.recommendation.adapter.outgoing.amqp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.bridge.core.recommendation.domain.model.RecommendationsStoredEvent;
import com.silenteight.bridge.core.recommendation.domain.port.outgoing.RecommendationEventPublisher;
import com.silenteight.bridge.core.recommendation.infrastructure.amqp.RecommendationOutgoingRecommendationsStoredConfigurationProperties;
import com.silenteight.proto.recommendation.api.v1.RecommendationsStored;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class RecommendationsStoredEventPublisher implements RecommendationEventPublisher {

  private final RabbitTemplate rabbitTemplate;
  private final RecommendationOutgoingRecommendationsStoredConfigurationProperties properties;

  @Override
  public void publish(RecommendationsStoredEvent event) {
    var message = RecommendationsStored.newBuilder()
        .setAnalysisName(event.analysisName())
        .addAllAlertNames(event.alertNames())
        .setIsTimedOut(event.isTimedOut())
        .build();

    log.info(
        "Send RecommendationsStoredEvent with [{}] alerts for analysis name [{}].",
        event.alertNames().size(),
        event.analysisName());

    rabbitTemplate.convertAndSend(properties.exchangeName(), "", message, rabbitMessage -> {
      rabbitMessage.getMessageProperties().setPriority(event.priority());
      return rabbitMessage;
    });
  }
}
