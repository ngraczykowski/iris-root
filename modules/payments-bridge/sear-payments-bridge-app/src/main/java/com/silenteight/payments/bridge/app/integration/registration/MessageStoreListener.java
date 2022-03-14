package com.silenteight.payments.bridge.app.integration.registration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.common.model.SimpleAlertId;
import com.silenteight.payments.bridge.common.resource.ResourceName;
import com.silenteight.payments.bridge.firco.alertmessage.port.FilterAlertMessageUseCase;
import com.silenteight.proto.payments.bridge.internal.v1.event.MessageStored;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import static com.silenteight.payments.bridge.app.amqp.AmqpDefaults.FIRCO_COMMAND_QUEUE_NAME;

@Slf4j
@Service
@RequiredArgsConstructor
class MessageStoreListener {

  private final FilterAlertMessageUseCase filterAlertMessageUseCase;
  private final AlertRegistrationInitialStep alertRegistrationInitialStep;


  @RabbitListener(queues = FIRCO_COMMAND_QUEUE_NAME)
  public void handle(final MessageStored messageStored) {
    log.info("Fetching data from rabbitmq: {}", messageStored);
    final SimpleAlertId simpleAlertId =
        new SimpleAlertId(ResourceName.create(messageStored.getAlert()).getUuid("alert-messages"));

    if (!this.filterAlertMessageUseCase.isResolvedOrOutdated(simpleAlertId) &&
        !this.filterAlertMessageUseCase.hasTooManyHits(simpleAlertId)) {
      this.alertRegistrationInitialStep.start(simpleAlertId.getAlertId());
    }
  }
}
