package com.silenteight.sens.webapp.backend.presentation.dto.branch.details;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import com.silenteight.sens.webapp.backend.presentation.dto.branch.DecisionTreeInfo;
import com.silenteight.sens.webapp.backend.presentation.dto.branch.FeatureDto;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class BranchDetailsDto {

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
  @NonNull
  private final BranchDetailsSummaryDto summary;
  @NonNull
  private final ChangeLogDto changeLog;
  private final Double score;
  private final boolean reviewed;
  private final Instant reviewedAt;
  private final String reviewedBy;
  private final boolean pendingChanges;
  private final String statusSetBy;
  private final Instant statusSetAt;
  private final String decisionSetBy;
  private final Instant decisionSetAt;
  private final boolean disabledByCircuitBreaker;
  private final LocalDateTime lastUsedAt;
}
