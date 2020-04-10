package com.silenteight.sens.webapp.backend.reasoningbranch.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.silenteight.sens.webapp.backend.reasoningbranch.BranchId;
import com.silenteight.sens.webapp.backend.reasoningbranch.update.UpdateBranchesCommand;

import java.util.List;
import javax.annotation.Nullable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BranchChangesRequestDto {

  @Nullable
  private String aiSolution;

  @Nullable
  private Boolean active;

  UpdateBranchesCommand toCommand(List<BranchId> branchIds) {
    return new UpdateBranchesCommand(branchIds, aiSolution, active);
  }
}
