package com.silenteight.connector.ftcc.ingest.adapter.outgoing.amqp;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.connector.ftcc.ingest.domain.port.outgoing.AlertMessage;

import org.springframework.integration.dsl.IntegrationFlowAdapter;
import org.springframework.integration.dsl.IntegrationFlowDefinition;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

import static com.silenteight.connector.ftcc.ingest.adapter.outgoing.amqp.DataPrepChannels.DATA_PREP_GATEWAY_CHANNEL;
import static com.silenteight.connector.ftcc.ingest.adapter.outgoing.amqp.DataPrepChannels.DATA_PREP_OUTBOUND_CHANNEL;
import static java.lang.Boolean.TRUE;

@Component
@Slf4j
class DataPrepIntegrationFlow extends IntegrationFlowAdapter {

  @Override
  protected IntegrationFlowDefinition<?> buildFlow() {
    return from(DATA_PREP_GATEWAY_CHANNEL)
        .enrichHeaders(enricher -> enricher
            .priorityFunction(DataPrepIntegrationFlow::getPriority, TRUE))
        .handle(AlertMessage.class, DataPrepIntegrationFlow::logMessage)
        .transform(AlertMessage::toAlertMessageStored)
        .channel(DATA_PREP_OUTBOUND_CHANNEL);
  }

  private static AlertMessage logMessage(AlertMessage message, MessageHeaders headers) {
    if (log.isDebugEnabled()) {
      log.debug("Sending alert message: batchName={}, messageName={}, state={}",
          message.getBatchName(), message.getMessageName(), message.getState());
    }

    return message;
  }

  private static Integer getPriority(Message<AlertMessage> message) {
    return message.getPayload().getPriority();
  }
}
