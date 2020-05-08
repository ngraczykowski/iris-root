package com.silenteight.sens.webapp.backend.reasoningbranch.update;

import lombok.RequiredArgsConstructor;

import com.silenteight.sens.webapp.backend.reasoningbranch.BranchesNotFoundException;
import com.silenteight.sens.webapp.backend.reasoningbranch.rest.BranchDto;
import com.silenteight.sens.webapp.backend.reasoningbranch.rest.ReasoningBranchesQuery;

import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections4.CollectionUtils.disjunction;

@RequiredArgsConstructor
class ReasoningBranchValidator {

  private final ReasoningBranchesQuery reasoningBranchesQuery;

  void validate(long treeId, List<Long> branchIds) {
    Collection<Long> nonExistingBranchIds = nonExistingBranchIdsOf(treeId, branchIds);
    if (!nonExistingBranchIds.isEmpty()) {
      throw new BranchesNotFoundException(nonExistingBranchIds);
    }
  }

  private Collection<Long> nonExistingBranchIdsOf(long treeId, List<Long> branchIds) {
    List<Long> existingBranchIds =
        reasoningBranchesQuery.findByTreeIdAndBranchIds(treeId, branchIds)
            .stream()
            .map(BranchDto::getReasoningBranchId)
            .collect(toList());

    return disjunction(branchIds, existingBranchIds);
  }
}
