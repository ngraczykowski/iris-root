package com.silenteight.sens.webapp.backend.bulkchange.closed;

import com.silenteight.sens.webapp.backend.bulkchange.BulkChangeDto;
import com.silenteight.sens.webapp.backend.bulkchange.BulkChangeIdsForReasoningBranchDto;
import com.silenteight.sens.webapp.backend.reasoningbranch.list.dto.ReasoningBranchIdDto;

import java.util.List;

public interface ClosedBulkChangeQuery {

  List<BulkChangeDto> listClosed();

  List<BulkChangeIdsForReasoningBranchDto> getIdsOfClosed(
      List<ReasoningBranchIdDto> reasoningBranchIds);
}
