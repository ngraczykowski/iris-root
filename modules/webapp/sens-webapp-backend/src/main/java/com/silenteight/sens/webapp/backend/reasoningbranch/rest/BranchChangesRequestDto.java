package com.silenteight.sens.webapp.backend.reasoningbranch.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.silenteight.sens.webapp.backend.reasoningbranch.BranchId;
import com.silenteight.sens.webapp.backend.reasoningbranch.update.UpdateBranchCommand;

import javax.annotation.Nullable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BranchChangesRequestDto {

  @Nullable
  private String aiSolution;

  @Nullable
  private Boolean isActive;

  UpdateBranchCommand toCommand(BranchId branchId) {
    return new UpdateBranchCommand(
        branchId,
        aiSolution,
        isActive
    );
  }
}
