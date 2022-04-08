package com.silenteight.bridge.core.recommendation.adapter.incoming.amqp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.bridge.core.recommendation.domain.RecommendationFacade;
import com.silenteight.bridge.core.recommendation.domain.command.ProceedBatchTimeoutCommand;
import com.silenteight.proto.registration.api.v1.MessageNotifyBatchTimedOut;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class NotifyBatchTimeoutRabbitAmqpListener {

  private final RecommendationFacade recommendationFacade;

  @RabbitListener(
      queues = "${amqp.recommendation.incoming.notify-batch-timeout.queue-name}",
      errorHandler = "recommendationAmqpErrorHandler"
  )
  public void subscribe(MessageNotifyBatchTimedOut message) {
    log.info(
        "Received NotifyBatchTimedOut amqp message for analysis={}", message.getAnalysisName());

    recommendationFacade.proceedBatchTimeout(
        new ProceedBatchTimeoutCommand(message.getAnalysisName(), message.getAlertNamesList()));
  }
}
