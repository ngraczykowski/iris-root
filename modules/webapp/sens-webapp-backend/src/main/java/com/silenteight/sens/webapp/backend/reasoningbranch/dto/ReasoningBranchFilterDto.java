package com.silenteight.sens.webapp.backend.reasoningbranch.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;

@Data
@RequiredArgsConstructor
public class ReasoningBranchFilterDto {

  @Nullable
  private final String aiSolution;
  @Nullable
  private final Boolean active;

  public boolean hasAiSolution() {
    return aiSolution != null;
  }

  public boolean hasEnablement() {
    return active != null;
  }
}
