package com.silenteight.serp.governance.bulkchange.audit;

import com.silenteight.proto.serp.v1.governance.BulkBranchChangeAppliedEvent;
import com.silenteight.proto.serp.v1.governance.BulkBranchChangeCreatedEvent;
import com.silenteight.proto.serp.v1.governance.BulkBranchChangeRejectedEvent;

public interface BulkChangeAuditable {

  void auditApplication(BulkBranchChangeAppliedEvent auditEvent);

  void auditCreation(BulkBranchChangeCreatedEvent auditEvent);

  void auditRejection(BulkBranchChangeRejectedEvent auditEvent);

}
