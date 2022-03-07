package com.silenteight.bridge.core.registration.adapter.incoming;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.bridge.core.registration.domain.RegistrationFacade;
import com.silenteight.bridge.core.registration.domain.command.VerifyBatchTimeoutCommand;
import com.silenteight.proto.registration.api.v1.MessageVerifyBatchTimeout;

import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class VerifyBatchTimeoutAmqpListener {

  private final RegistrationFacade registrationFacade;

  /*
  Temporarily commented out due to ALL-657. Will be reimplemented in ALL-655
  @RabbitListener(
      queues = "${amqp.registration.incoming.verify-batch-timeout.queue-name}",
      errorHandler = "registrationAmqpErrorHandler")
   */
  void verifyBatchTimeout(MessageVerifyBatchTimeout message) {
    log.info("Received a message that batch with id [{}] is timed out. "
        + "Proceeding to verify whether it is still processing", message.getBatchId());

    var command = new VerifyBatchTimeoutCommand(message.getBatchId());
    registrationFacade.verifyBatchTimeout(command);
  }
}
