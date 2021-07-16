package com.silenteight.serp.governance.qa.manage.validation.view;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import com.silenteight.serp.governance.qa.manage.domain.DecisionLevel;

@Builder
@Value
public class ViewDecisionCommand {

  @NonNull
  String discriminator;
  @NonNull
  DecisionLevel level;
}
