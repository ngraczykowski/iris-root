package com.silenteight.sens.webapp.backend.reasoningbranch.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import com.silenteight.sens.webapp.backend.reasoningbranch.BranchId;
import com.silenteight.sens.webapp.backend.reasoningbranch.update.UpdateBranchesCommand;

import java.util.List;
import javax.annotation.Nullable;

import static java.util.stream.Collectors.toList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BranchesChangesRequestDto {

  @NonNull
  private List<Long> branchIds;

  @Nullable
  private String aiSolution;

  @Nullable
  private Boolean active;

  UpdateBranchesCommand toCommand(long treeId) {
    return new UpdateBranchesCommand(toBranchIds(treeId), aiSolution, active);
  }

  private List<BranchId> toBranchIds(long treeId) {
    return branchIds
        .stream()
        .map(branchId -> BranchId.of(treeId, branchId))
        .collect(toList());
  }
}
