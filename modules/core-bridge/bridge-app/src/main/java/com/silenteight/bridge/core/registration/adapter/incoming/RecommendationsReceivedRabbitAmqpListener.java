package com.silenteight.bridge.core.registration.adapter.incoming;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.bridge.core.registration.domain.RegistrationFacade;
import com.silenteight.bridge.core.registration.domain.command.MarkAlertsAsRecommendedCommand;
import com.silenteight.proto.recommendation.api.v1.RecommendationsStored;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class RecommendationsReceivedRabbitAmqpListener {

  private final RegistrationFacade registrationFacade;

  @RabbitListener(
      queues = "${amqp.registration.incoming.recommendation-received.queue-name}",
      errorHandler = "registrationAmqpErrorHandler"
  )
  public void recommendationReceived(RecommendationsStored recommendation) {
    log.info(
        "Received RecommendationsStored amqp message for analysis name={}",
        recommendation.getAnalysisName());
    registrationFacade.markAlertsAsRecommended(
        createMarkAlertsAsRecommendedCommand(recommendation));
  }

  private MarkAlertsAsRecommendedCommand createMarkAlertsAsRecommendedCommand(
      RecommendationsStored recommendation) {
    return new MarkAlertsAsRecommendedCommand(
        recommendation.getAnalysisName(),
        recommendation.getAlertNamesList());
  }
}
