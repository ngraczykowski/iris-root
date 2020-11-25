package com.silenteight.serp.governance.bulkchange.integration;

import lombok.RequiredArgsConstructor;

import com.silenteight.proto.serp.v1.governance.BulkBranchChangeRejectedEvent;
import com.silenteight.proto.serp.v1.governance.RejectBulkBranchChangeCommand;
import com.silenteight.serp.governance.bulkchange.BulkChangeCommands;
import com.silenteight.serp.governance.bulkchange.audit.BulkChangeAuditable;

import org.springframework.integration.dsl.IntegrationFlowAdapter;
import org.springframework.integration.dsl.IntegrationFlowDefinition;

import static com.silenteight.serp.governance.bulkchange.integration.BulkChangeIntegrationModule.REJECT_BULK_CHANGE_INBOUND_CHANNEL;
import static com.silenteight.serp.governance.bulkchange.integration.BulkChangeIntegrationModule.REJECT_BULK_CHANGE_OUTBOUND_CHANNEL;

@RequiredArgsConstructor
class RejectBulkChangeIntegrationFlowAdapter extends IntegrationFlowAdapter {

  private final BulkChangeAuditable bulkChangeAuditable;
  private final BulkChangeCommands bulkChange;

  @Override
  protected IntegrationFlowDefinition<?> buildFlow() {
    return from(REJECT_BULK_CHANGE_INBOUND_CHANNEL)
        .handle(RejectBulkBranchChangeCommand.class, (payload, headers) ->
            bulkChange.rejectBulkBranchChange(payload)
        )
        // BS
        .handle(BulkBranchChangeRejectedEvent.class, (payload, headers) -> {
          bulkChangeAuditable.auditRejection(payload);
          return payload;
        })
        .channel(REJECT_BULK_CHANGE_OUTBOUND_CHANNEL);
  }
}
