package com.silenteight.sens.webapp.backend.deprecated.reasoningbranch.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import com.silenteight.sens.webapp.backend.deprecated.reasoningbranch.update.UpdateBranchesCommand;

import java.util.List;
import javax.annotation.Nullable;

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

  @Nullable
  private String comment;

  UpdateBranchesCommand toCommand(long treeId) {
    return new UpdateBranchesCommand(treeId, branchIds, aiSolution, active, comment);
  }
}
