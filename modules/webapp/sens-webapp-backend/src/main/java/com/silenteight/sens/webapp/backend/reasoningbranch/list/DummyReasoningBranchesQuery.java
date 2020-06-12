package com.silenteight.sens.webapp.backend.reasoningbranch.list;

import com.silenteight.sens.webapp.backend.reasoningbranch.dto.ReasoningBranchFilterDto;
import com.silenteight.sens.webapp.backend.reasoningbranch.dto.ReasoningBranchesPageDto;
import com.silenteight.sens.webapp.backend.support.Paging;

import static java.util.Collections.emptyList;

class DummyReasoningBranchesQuery implements ReasoningBranchesQuery {

  @Override
  public ReasoningBranchesPageDto list(ReasoningBranchFilterDto filter, Paging paging) {
    return new ReasoningBranchesPageDto(emptyList(), 0);
  }
}
