package com.silenteight.serp.governance.bulkchange;

import com.silenteight.proto.serp.v1.governance.*;

public interface BulkChangeCommands {

  // BS
  String BULK_CHANGE_ENTITY_CLASS_NAME = BulkBranchChange.class.getName();

  BulkBranchChangeAppliedEvent applyBulkBranchChange(ApplyBulkBranchChangeCommand command);

  BulkBranchChangeCreatedEvent createBulkBranchChange(CreateBulkBranchChangeCommand command);

  BulkBranchChangeRejectedEvent rejectBulkBranchChange(RejectBulkBranchChangeCommand command);
}
