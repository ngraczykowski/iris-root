package com.silenteight.serp.governance.qa.analysis.update.dto;

import lombok.Builder;
import lombok.Data;

import com.silenteight.serp.governance.qa.domain.DecisionState;

import javax.validation.constraints.NotNull;

@Builder
@Data
public class UpdateAnalysisDecisionDto {

  @NotNull
  private DecisionState decision;
  @NotNull
  private String comment;
}
