package com.silenteight.serp.governance.bulkchange.audit;

import lombok.RequiredArgsConstructor;

import com.silenteight.proto.serp.v1.governance.BulkBranchChangeAppliedEvent;
import com.silenteight.proto.serp.v1.governance.BulkBranchChangeCreatedEvent;
import com.silenteight.proto.serp.v1.governance.BulkBranchChangeRejectedEvent;

@RequiredArgsConstructor
class BulkChangeAudit implements BulkChangeAuditable {

  private final AuditBulkChangeApplicationHandler applicationHandler;
  private final AuditBulkChangeCreationHandler creationHandler;
  private final AuditBulkChangeRejectionHandler rejectionHandler;

  @Override
  public void auditApplication(BulkBranchChangeAppliedEvent auditEvent) {
    applicationHandler.handle(auditEvent);
  }

  @Override
  public void auditCreation(BulkBranchChangeCreatedEvent auditEvent) {
    creationHandler.handle(auditEvent);
  }

  @Override
  public void auditRejection(BulkBranchChangeRejectedEvent auditEvent) {
    rejectionHandler.handle(auditEvent);
  }
}
