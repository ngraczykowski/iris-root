package com.silenteight.serp.governance.bulkchange.integration;

import lombok.RequiredArgsConstructor;

import com.silenteight.proto.serp.v1.governance.BulkBranchChangeCreatedEvent;
import com.silenteight.proto.serp.v1.governance.CreateBulkBranchChangeCommand;
import com.silenteight.serp.governance.bulkchange.BulkChangeCommands;
import com.silenteight.serp.governance.bulkchange.audit.BulkChangeAuditable;

import org.springframework.integration.dsl.IntegrationFlowAdapter;
import org.springframework.integration.dsl.IntegrationFlowDefinition;

import static com.silenteight.serp.governance.bulkchange.integration.BulkChangeIntegrationModule.CREATE_BULK_CHANGE_INBOUND_CHANNEL;
import static com.silenteight.serp.governance.bulkchange.integration.BulkChangeIntegrationModule.CREATE_BULK_CHANGE_OUTBOUND_CHANNEL;

@RequiredArgsConstructor
class CreateBulkChangeIntegrationFlowAdapter extends IntegrationFlowAdapter {

  private final BulkChangeAuditable bulkChangeAuditable;
  private final BulkChangeCommands bulkChange;

  @Override
  protected IntegrationFlowDefinition<?> buildFlow() {
    return from(CREATE_BULK_CHANGE_INBOUND_CHANNEL)
        .handle(
            CreateBulkBranchChangeCommand.class,
            (payload, headers) -> bulkChange.createBulkBranchChange(payload)
        )
        // BS
        .handle(BulkBranchChangeCreatedEvent.class, (payload, headers) -> {
          bulkChangeAuditable.auditCreation(payload);
          return payload;
        })
        .channel(CREATE_BULK_CHANGE_OUTBOUND_CHANNEL);
  }
}
