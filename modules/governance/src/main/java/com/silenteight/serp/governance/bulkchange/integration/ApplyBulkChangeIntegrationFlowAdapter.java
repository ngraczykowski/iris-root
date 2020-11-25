package com.silenteight.serp.governance.bulkchange.integration;

import lombok.RequiredArgsConstructor;

import com.silenteight.proto.serp.v1.governance.ApplyBulkBranchChangeCommand;
import com.silenteight.proto.serp.v1.governance.BulkBranchChangeAppliedEvent;
import com.silenteight.serp.governance.bulkchange.BulkChangeCommands;
import com.silenteight.serp.governance.bulkchange.audit.BulkChangeAuditable;

import org.springframework.integration.dsl.IntegrationFlowAdapter;
import org.springframework.integration.dsl.IntegrationFlowDefinition;

import static com.silenteight.serp.governance.bulkchange.integration.BulkChangeIntegrationModule.APPLY_BULK_CHANGE_INBOUND_CHANNEL;
import static com.silenteight.serp.governance.bulkchange.integration.BulkChangeIntegrationModule.APPLY_BULK_CHANGE_OUTBOUND_CHANNEL;

@RequiredArgsConstructor
class ApplyBulkChangeIntegrationFlowAdapter extends IntegrationFlowAdapter {

  private final BulkChangeAuditable bulkChangeAuditable;
  private final BulkChangeCommands bulkChange;

  @Override
  protected IntegrationFlowDefinition<?> buildFlow() {
    return from(APPLY_BULK_CHANGE_INBOUND_CHANNEL)
        .handle(
            ApplyBulkBranchChangeCommand.class,
            (payload, headers) -> bulkChange.applyBulkBranchChange(payload)
        )
        // BS
        .handle(BulkBranchChangeAppliedEvent.class, (payload, headers) -> {
          bulkChangeAuditable.auditApplication(payload);
          return payload;
        })
        .channel(APPLY_BULK_CHANGE_OUTBOUND_CHANNEL);
  }
}
