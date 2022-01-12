package com.silenteight.bridge.core.registration.adapter.incoming;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.bridge.core.registration.domain.MarkAlertsAsRecommendedCommand;
import com.silenteight.bridge.core.registration.domain.RegistrationFacade;
import com.silenteight.proto.recommendation.api.v1.RecommendationsReceived;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class RecommendationsReceivedRabbitAmqpListener {

  private final RegistrationFacade registrationFacade;

  @RabbitListener(queues = "${amqp.registration.incoming.recommendation-received.queue-name}")
  public void recommendationReceived(RecommendationsReceived recommendation) {
    log.info(
        "Received RecommendationsReceived amqp message for analysis id={}",
        recommendation.getAnalysisId());
    registrationFacade.markAlertsAsRecommended(
        createMarkAlertsAsRecommendedCommand(recommendation));
  }

  private MarkAlertsAsRecommendedCommand createMarkAlertsAsRecommendedCommand(
      RecommendationsReceived recommendation) {
    return new MarkAlertsAsRecommendedCommand(
        recommendation.getAnalysisId(),
        recommendation.getAlertIdsList());
  }
}
