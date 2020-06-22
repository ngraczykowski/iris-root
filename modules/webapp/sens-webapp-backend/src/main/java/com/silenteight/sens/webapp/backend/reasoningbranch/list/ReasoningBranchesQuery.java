package com.silenteight.sens.webapp.backend.reasoningbranch.list;

import com.silenteight.sens.webapp.backend.reasoningbranch.list.dto.ReasoningBranchFilterDto;
import com.silenteight.sens.webapp.backend.reasoningbranch.list.dto.ReasoningBranchesPageDto;
import com.silenteight.sens.webapp.backend.support.Paging;

public interface ReasoningBranchesQuery {

  ReasoningBranchesPageDto list(ReasoningBranchFilterDto filter, Paging paging);
}
