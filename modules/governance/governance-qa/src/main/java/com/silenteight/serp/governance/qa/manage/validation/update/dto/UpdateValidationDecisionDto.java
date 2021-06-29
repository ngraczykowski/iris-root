package com.silenteight.serp.governance.qa.manage.validation.update.dto;

import lombok.Builder;
import lombok.Data;

import com.silenteight.serp.governance.qa.manage.domain.DecisionState;

import javax.validation.constraints.NotNull;

@Builder
@Data
public class UpdateValidationDecisionDto {

  @NotNull
  private DecisionState decision;
  @NotNull
  private String comment;
}
