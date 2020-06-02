package com.silenteight.sens.webapp.backend.changerequest.approve;

import lombok.RequiredArgsConstructor;

import com.silenteight.proto.serp.v1.changerequest.ApproveChangeRequestCommand;

import org.springframework.integration.dsl.IntegrationFlowAdapter;
import org.springframework.integration.dsl.IntegrationFlowDefinition;

import static com.silenteight.sens.webapp.backend.bulkchange.BulkChangeIntegrationChannels.APPLY_BULK_CHANGE_OUTBOUND_CHANNEL;
import static com.silenteight.sens.webapp.backend.changerequest.approve.ChangeRequestApprovalIntegrationChannels.APPROVE_CHANGE_REQUEST_INBOUND_CHANNEL;

@RequiredArgsConstructor
class ChangeRequestApprovalIntegrationFlowAdapter extends IntegrationFlowAdapter {

  private final ApproveChangeRequestMessageHandler approveChangeRequestMessageHandler;

  @Override
  protected IntegrationFlowDefinition<?> buildFlow() {
    return from(APPROVE_CHANGE_REQUEST_INBOUND_CHANNEL)
        .handle(
            ApproveChangeRequestCommand.class,
            (p, h) -> approveChangeRequestMessageHandler.handle(p))
        .channel(APPLY_BULK_CHANGE_OUTBOUND_CHANNEL);
  }
}
