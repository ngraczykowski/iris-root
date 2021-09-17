package com.silenteight.payments.bridge.firco.callback;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.firco.dto.output.AlertDecisionMessageDto;

import org.springframework.integration.dsl.IntegrationFlowAdapter;
import org.springframework.integration.dsl.IntegrationFlowDefinition;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

import static com.silenteight.payments.bridge.firco.core.alertmessage.integration.AlertMessageChannels.ALERT_MESSAGE_RESPONSE_CHANNEL;
import static com.silenteight.payments.bridge.firco.core.alertmessage.integration.AlertMessageChannels.ALERT_MESSAGE_RESPONSE_OUTBOUND_CHANNEL;

@RequiredArgsConstructor
@Component
@Slf4j
class OutboundGatewayIntegrationFlow extends IntegrationFlowAdapter {

  private final ClientRequestDtoFactory clientRequestDtoFactory;

  @Override
  protected IntegrationFlowDefinition<?> buildFlow() {
    return from(ALERT_MESSAGE_RESPONSE_CHANNEL)
        .handle(AlertDecisionMessageDto.class, this::logRequest)
        .transform(clientRequestDtoFactory::create)
        .channel(ALERT_MESSAGE_RESPONSE_OUTBOUND_CHANNEL);
  }

  private AlertDecisionMessageDto logRequest(
      AlertDecisionMessageDto payload, MessageHeaders headers) {
    if (log.isDebugEnabled()) {
      log.debug("Sending AlertMessage [{}] decision back to the requesting party request",
          payload.getMessageId());
    }
    return payload;
  }

}
