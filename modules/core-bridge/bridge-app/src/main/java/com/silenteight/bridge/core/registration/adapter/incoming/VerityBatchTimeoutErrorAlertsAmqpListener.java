package com.silenteight.bridge.core.registration.adapter.incoming;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.bridge.core.registration.domain.RegistrationFacade;
import com.silenteight.bridge.core.registration.domain.command.VerifyBatchTimeoutCommand;
import com.silenteight.proto.registration.api.v1.MessageVerifyBatchTimeout;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class VerityBatchTimeoutErrorAlertsAmqpListener {

  private final RegistrationFacade registrationFacade;

  @RabbitListener(
      queues = "${amqp.registration.incoming.verify-batch-timeout.error-alerts-queue-name}",
      errorHandler = "registrationAmqpErrorHandler")
  void verifyBatchTimeoutAlerts(MessageVerifyBatchTimeout message) {
    log.info("Received a message that batch with id [{}] is timed out. "
        + "Proceeding to verify whether all alerts are erroneous", message.getBatchId());

    var command = new VerifyBatchTimeoutCommand(message.getBatchId());
    registrationFacade.verifyBatchTimeoutForAllErroneousAlerts(command);
  }
}
