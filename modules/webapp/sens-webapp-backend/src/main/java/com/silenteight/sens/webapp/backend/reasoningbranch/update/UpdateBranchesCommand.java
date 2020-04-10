package com.silenteight.sens.webapp.backend.reasoningbranch.update;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import com.silenteight.sens.webapp.backend.reasoningbranch.BranchId;

import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;

import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class UpdateBranchesCommand implements UpdatedBranches {

  @NonNull
  private final List<BranchId> branchIds;

  @Nullable
  private final String newAiSolution;

  @Nullable
  private final Boolean newIsActive;

  boolean doesNotHaveChanges() {
    return getNewAiSolution().isEmpty() && getNewStatus().isEmpty();
  }

  @Override
  public Optional<String> getNewAiSolution() {
    return ofNullable(newAiSolution);
  }

  @Override
  public Optional<Boolean> getNewStatus() {
    return ofNullable(newIsActive);
  }

  @Override
  public List<BranchId> getBranchIds() {
    return branchIds;
  }

}
