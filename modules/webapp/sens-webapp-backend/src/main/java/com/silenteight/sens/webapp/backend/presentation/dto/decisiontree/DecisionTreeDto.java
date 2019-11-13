package com.silenteight.sens.webapp.backend.presentation.dto.decisiontree;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import com.silenteight.sens.webapp.backend.presentation.dto.decisiontree.details.DecisionTreeSummaryDto;
import com.silenteight.sens.webapp.backend.presentation.dto.decisiontree.status.StatusDto;
import com.silenteight.sens.webapp.backend.presentation.dto.model.ModelDto;
import com.silenteight.sens.webapp.kernel.security.SensPermission;

import java.util.List;
import java.util.Set;

@Data
@Builder
public class DecisionTreeDto {

  @NonNull
  private final Long id;
  @NonNull
  private final String name;
  private final boolean active;
  @NonNull
  private final StatusDto status;
  @NonNull
  private final ModelDto model;
  private final DecisionTreeSummaryDto summary;
  @NonNull
  private final List<AgentDto> agents;
  private final int activeReasoningBranches;
  private final int totalReasoningBranches;
  @NonNull
  private final List<String> assignments;
  @NonNull
  private final List<String> activations;
  private final int matchAlerts;

  private final Set<SensPermission> permissions;

  private final List<FeatureDto> features;
}
