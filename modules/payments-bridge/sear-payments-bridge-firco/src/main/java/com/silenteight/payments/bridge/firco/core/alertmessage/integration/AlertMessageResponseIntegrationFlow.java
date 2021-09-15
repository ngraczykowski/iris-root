package com.silenteight.payments.bridge.firco.core.alertmessage.integration;

import lombok.extern.slf4j.Slf4j;

import org.springframework.integration.dsl.IntegrationFlowAdapter;
import org.springframework.integration.dsl.IntegrationFlowDefinition;

//@Component
@Slf4j
class AlertMessageResponseIntegrationFlow extends IntegrationFlowAdapter {

  @Override
  protected IntegrationFlowDefinition<?> buildFlow() {
    // TODO: integration mock
    return from(AlertMessageChannels.ALERT_MESSAGE_RESPONSE_CHANNEL)
        .channel(AlertMessageChannels.ALERT_MESSAGE_RESPONSE_OUTBOUND_CHANNEL);
  }
}
