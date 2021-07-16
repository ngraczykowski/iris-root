package com.silenteight.serp.governance.qa.send.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import com.silenteight.serp.governance.qa.manage.domain.DecisionLevel;
import com.silenteight.serp.governance.qa.manage.domain.DecisionState;

@Builder
@Data
public class AlertDto {

  @NonNull
  private String discriminator;
  @NonNull
  private DecisionLevel level;
  @NonNull
  private DecisionState state;
  private String comment;
}
