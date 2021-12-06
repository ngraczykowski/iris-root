package com.silenteight.adjudication.engine.analysis.pii.integration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.analysis.pii.PiiFacade;
import com.silenteight.dataretention.api.v1.PersonalInformationExpired;

import org.springframework.integration.dsl.IntegrationFlowAdapter;
import org.springframework.integration.dsl.IntegrationFlowDefinition;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

import static com.silenteight.adjudication.engine.analysis.pii.integration.RemovePiiChannels.REMOVE_PII_INBOUND_CHANNEL;

@RequiredArgsConstructor
@Component
@Slf4j
class RemovePiiIntegrationFlow extends IntegrationFlowAdapter {

  private final PiiFacade facade;

  @Override
  protected IntegrationFlowDefinition<?> buildFlow() {
    return from(REMOVE_PII_INBOUND_CHANNEL)
        .handle(PersonalInformationExpired.class, this::handleResponse);
  }

  private int handleResponse(PersonalInformationExpired payload, MessageHeaders headers) {

    if (log.isDebugEnabled()) {
      log.debug("Received alerts with expired pii = {}", payload.getAlertsList());
    }

    facade.removePii(payload.getAlertsList());
    return 0;
  }
}
