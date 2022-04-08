package com.silenteight.bridge.core.registration.adapter.incoming.amqp;

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
class RecommendationsStoredRabbitAmqpListener {

  private final RegistrationFacade registrationFacade;

  @RabbitListener(
      queues = "${amqp.registration.incoming.recommendation-stored.queue-name}",
      errorHandler = "registrationAmqpErrorHandler"
  )
  public void recommendationsStored(RecommendationsStored recommendation) {
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
