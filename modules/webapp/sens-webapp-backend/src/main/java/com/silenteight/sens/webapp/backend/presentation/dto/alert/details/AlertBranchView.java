package com.silenteight.sens.webapp.backend.presentation.dto.alert.details;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import com.silenteight.sens.webapp.backend.presentation.dto.branch.DecisionTreeInfo;
import com.silenteight.sens.webapp.backend.presentation.dto.branch.FeatureDto;

import java.util.List;

@Builder
@Data
public class AlertBranchView {

  @NonNull
  private final DecisionTreeInfo decisionTreeInfo;
  @NonNull
  private final Long matchGroupId;
  @NonNull
  private final List<FeatureDto> features;
  @NonNull
  private final String solution;
}
