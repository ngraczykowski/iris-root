package com.silenteight.serp.governance.qa.analysis.view;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import com.silenteight.serp.governance.qa.domain.DecisionLevel;

@Builder
@Value
public class ViewDecisionCommand {

  @NonNull
  String alertName;
  @NonNull
  DecisionLevel level;
}
