package com.silenteight.payments.bridge.firco.core.alertmessage.integration;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.firco.core.alertmessage.model.FircoAlertMessage;

import org.springframework.integration.dsl.IntegrationFlowAdapter;
import org.springframework.integration.dsl.IntegrationFlowDefinition;
import org.springframework.messaging.MessageHeaders;

//@Component
@Slf4j
class AlertMessageIntegrationFlow extends IntegrationFlowAdapter {

  @Override
  protected IntegrationFlowDefinition<?> buildFlow() {
    // TODO: integration mock
    return from(AlertMessageChannels.ALERT_MESSAGE_STORED_REQUEST_CHANNEL)
        .handle(FircoAlertMessage.class, this::logRequest)
        .transform(FircoAlertMessage::toRequest)
        .channel(AlertMessageChannels.ALERT_MESSAGE_STORED_REQUEST_OUTBOUND_CHANNEL);
  }

  private FircoAlertMessage logRequest(FircoAlertMessage payload, MessageHeaders headers) {
    if (log.isDebugEnabled()) {
      log.debug("Sending request for recommendation: alertMessageId={}", payload.getId());
    }
    return payload;
  }

}
