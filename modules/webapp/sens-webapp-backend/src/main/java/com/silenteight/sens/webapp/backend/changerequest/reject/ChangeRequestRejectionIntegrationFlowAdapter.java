package com.silenteight.sens.webapp.backend.changerequest.reject;

import lombok.RequiredArgsConstructor;

import com.silenteight.proto.serp.v1.changerequest.RejectChangeRequestCommand;

import org.springframework.integration.dsl.IntegrationFlowAdapter;
import org.springframework.integration.dsl.IntegrationFlowDefinition;

import static com.silenteight.sens.webapp.backend.bulkchange.BulkChangeIntegrationChannels.REJECT_BULK_CHANGE_OUTBOUND_CHANNEL;
import static com.silenteight.sens.webapp.backend.changerequest.reject.ChangeRequestRejectionIntegrationChannels.REJECT_CHANGE_REQUEST_INBOUND_CHANNEL;

@RequiredArgsConstructor
class ChangeRequestRejectionIntegrationFlowAdapter extends IntegrationFlowAdapter {

  private final RejectChangeRequestMessageHandler rejectChangeRequestMessageHandler;

  @Override
  protected  IntegrationFlowDefinition<?> buildFlow() {
    return from(REJECT_CHANGE_REQUEST_INBOUND_CHANNEL)
        .handle(
            RejectChangeRequestCommand.class,
            (p, h) -> rejectChangeRequestMessageHandler.handle(p))
        .channel(REJECT_BULK_CHANGE_OUTBOUND_CHANNEL)
        .log();
  }
}
