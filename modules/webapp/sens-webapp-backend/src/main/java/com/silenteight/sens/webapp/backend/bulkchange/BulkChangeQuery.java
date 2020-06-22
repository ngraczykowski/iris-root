package com.silenteight.sens.webapp.backend.bulkchange;

import com.silenteight.sens.webapp.backend.reasoningbranch.list.dto.ReasoningBranchIdDto;

import java.util.List;

public interface BulkChangeQuery {

  List<BulkChangeDto> listPending();

  List<BulkChangeIdsForReasoningBranchDto> getIds(List<ReasoningBranchIdDto> reasoningBranchIds);
}
