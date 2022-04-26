package com.silenteight.bridge.core.registration.adapter.incoming.amqp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.bridge.core.registration.domain.RegistrationFacade;
import com.silenteight.bridge.core.registration.domain.command.MarkAlertsAsDeliveredCommand;
import com.silenteight.proto.recommendation.api.v1.RecommendationsDelivered;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class RecommendationsDeliveredRabbitAmqpListener {

  private final RegistrationFacade registrationFacade;

  @RabbitListener(
      queues = "${amqp.registration.incoming.recommendation-delivered.queue-name}",
      errorHandler = "registrationAmqpErrorHandler"
  )
  public void recommendationDelivered(RecommendationsDelivered recommendation) {
    log.info(
        "Received RecommendationsDelivered with batch id [{}] and analysis name [{}].",
        recommendation.getBatchId(), recommendation.getAnalysisName());
    registrationFacade.markAlertsAsDelivered(createMarkAlertsAsDeliveredCommand(recommendation));
  }

  private MarkAlertsAsDeliveredCommand createMarkAlertsAsDeliveredCommand(
      RecommendationsDelivered recommendation) {
    return MarkAlertsAsDeliveredCommand.builder()
        .batchId(recommendation.getBatchId())
        .analysisName(recommendation.getAnalysisName())
        .alertNames(recommendation.getAlertNamesList())
        .build();
  }
}
