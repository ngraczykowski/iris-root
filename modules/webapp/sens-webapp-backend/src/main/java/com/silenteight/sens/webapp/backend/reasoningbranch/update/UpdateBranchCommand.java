package com.silenteight.sens.webapp.backend.reasoningbranch.update;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import com.silenteight.sens.webapp.backend.reasoningbranch.BranchId;

import java.util.Optional;
import javax.annotation.Nullable;

import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class UpdateBranchCommand implements UpdatedBranch {

  private final BranchId branchId;

  @Nullable
  private final String newAiSolution;

  @Nullable
  private final Boolean newIsActive;

  @Override
  public BranchId getBranchId() {
    return branchId;
  }

  @Override
  public Optional<String> getNewAiSolution() {
    return ofNullable(newAiSolution);
  }

  @Override
  public Optional<Boolean> getNewIsActive() {
    return ofNullable(newIsActive);
  }
}
