package com.silenteight.sens.webapp.backend.reasoningbranch.report;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@Builder
public class BranchWithFeaturesDto {

  private final long reasoningBranchId;

  private final Instant updatedAt;

  private final String aiSolution;

  private final boolean isActive;

  private final List<String> featureValues;

  public String featureValue(int index) {
    return featureValues.get(index);
  }
}
