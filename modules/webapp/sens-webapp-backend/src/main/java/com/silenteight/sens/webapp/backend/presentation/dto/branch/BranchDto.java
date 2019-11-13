package com.silenteight.sens.webapp.backend.presentation.dto.branch;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class BranchDto {

  private final boolean active;
  private final boolean enabled;
  @NonNull
  private final DecisionTreeInfo decisionTreeInfo;
  @NonNull
  private final Long matchGroupId;
  @NonNull
  private final List<FeatureDto> features;
  @NonNull
  private final String solution;
  private final Double score;
  private final String reviewedBy;
  private final boolean reviewed;
  private final Instant reviewedAt;
  private final boolean pendingChanges;
  private final LocalDateTime lastUsedAt;
}
