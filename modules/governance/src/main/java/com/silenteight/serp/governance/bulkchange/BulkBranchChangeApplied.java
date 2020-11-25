package com.silenteight.serp.governance.bulkchange;

import lombok.*;

import com.silenteight.proto.serp.v1.recommendation.BranchSolution;
import com.silenteight.sep.base.common.entity.BaseEvent;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Nullable;

@Data
@Builder
@EqualsAndHashCode(callSuper = true, doNotUseGetters = true)
@ToString(callSuper = true, doNotUseGetters = true)
public class BulkBranchChangeApplied extends BaseEvent {

  private static final long serialVersionUID = -8556292458350367740L;

  private final UUID bulkBranchChangeId;

  private final UUID correlationId;

  @Nullable
  private final BranchSolution solutionChange;

  @Nullable
  private final Boolean enablementChange;

  @Singular
  private final Set<ReasoningBranchIdToApply> reasoningBranchIds;

  public Optional<BranchSolution> getSolutionChange() {
    return Optional.ofNullable(solutionChange);
  }

  public Optional<Boolean> getEnablementChange() {
    return Optional.ofNullable(enablementChange);
  }

  private final OffsetDateTime appliedAt;
}
