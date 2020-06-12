package com.silenteight.sens.webapp.backend.deprecated.reasoningbranch.update;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;

import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class UpdateBranchesCommand implements UpdatedBranches {

  private final long treeId;

  @NonNull
  private final List<Long> branchIds;

  @Nullable
  private final String newAiSolution;

  @Nullable
  private final Boolean newIsActive;

  @Nullable
  private final String comment;

  boolean doesNotHaveChanges() {
    return getNewAiSolution().isEmpty() && getNewStatus().isEmpty();
  }

  @Override
  public long getTreeId() {
    return treeId;
  }

  @Override
  public List<Long> getBranchIds() {
    return branchIds;
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
  public Optional<String> getComment() {
    return ofNullable(comment);
  }
}
