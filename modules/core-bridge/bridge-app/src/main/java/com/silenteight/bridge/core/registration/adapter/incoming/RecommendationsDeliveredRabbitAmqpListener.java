package com.silenteight.bridge.core.registration.adapter.incoming;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.bridge.core.registration.domain.MarkBatchAsDeliveredCommand;
import com.silenteight.bridge.core.registration.domain.RegistrationFacade;
import com.silenteight.proto.recommendation.api.v1.RecommendationsDelivered;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class RecommendationsDeliveredRabbitAmqpListener {

  private final RegistrationFacade registrationFacade;

  @RabbitListener(queues = "${amqp.registration.incoming.recommendation-delivered.queue-name}")
  public void recommendationDelivered(RecommendationsDelivered recommendation) {
    log.info(
        "Received RecommendationsDelivered amqp batch id={}", recommendation.getBatchId());
    registrationFacade.markBatchAsDelivered(createMarkAlertsAsDeliveredCommand(recommendation));
  }

  private MarkBatchAsDeliveredCommand createMarkAlertsAsDeliveredCommand(
      RecommendationsDelivered recommendation) {
    return new MarkBatchAsDeliveredCommand(recommendation.getBatchId());
  }
}
