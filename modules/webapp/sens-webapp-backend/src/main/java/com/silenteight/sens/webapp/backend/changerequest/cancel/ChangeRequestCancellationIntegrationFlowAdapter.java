package com.silenteight.sens.webapp.backend.changerequest.cancel;

import lombok.RequiredArgsConstructor;

import com.silenteight.proto.serp.v1.changerequest.CancelChangeRequestCommand;

import org.springframework.integration.dsl.IntegrationFlowAdapter;
import org.springframework.integration.dsl.IntegrationFlowDefinition;

import static com.silenteight.sens.webapp.backend.bulkchange.BulkChangeIntegrationChannels.REJECT_BULK_CHANGE_OUTBOUND_CHANNEL;
import static com.silenteight.sens.webapp.backend.changerequest.cancel.ChangeRequestCancellationIntegrationChannels.CANCEL_CHANGE_REQUEST_INBOUND_CHANNEL;

@RequiredArgsConstructor
public class ChangeRequestCancellationIntegrationFlowAdapter extends IntegrationFlowAdapter {

  private final CancelChangeRequestMessageHandler cancelChangeRequestMessageHandler;

  @Override
  protected IntegrationFlowDefinition<?> buildFlow() {
    return from(CANCEL_CHANGE_REQUEST_INBOUND_CHANNEL)
        .handle(
            CancelChangeRequestCommand.class,
            (p, h) -> cancelChangeRequestMessageHandler.handle(p))
        .channel(REJECT_BULK_CHANGE_OUTBOUND_CHANNEL);
  }
}
