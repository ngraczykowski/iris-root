package com.silenteight.serp.governance.qa.manage.analysis.update.dto;

import lombok.Builder;
import lombok.Data;

import com.silenteight.serp.governance.qa.manage.domain.DecisionState;

import javax.validation.constraints.NotNull;

@Builder
@Data
public class UpdateAnalysisDecisionDto {

  @NotNull
  private DecisionState state;
  @NotNull
  private String comment;
}
