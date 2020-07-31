package com.silenteight.sens.webapp.backend.bulkchange.pending;

import com.silenteight.sens.webapp.backend.bulkchange.BulkChangeDto;
import com.silenteight.sens.webapp.backend.bulkchange.BulkChangeIdsForReasoningBranchDto;
import com.silenteight.sens.webapp.backend.reasoningbranch.list.dto.ReasoningBranchIdDto;

import java.util.List;

public interface PendingBulkChangeQuery {

  List<BulkChangeDto> listPending();

  List<BulkChangeIdsForReasoningBranchDto> getIdsOfPending(
      List<ReasoningBranchIdDto> reasoningBranchIds);
}
