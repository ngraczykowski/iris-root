package com.silenteight.serp.governance.strategy.solve;

import lombok.NonNull;
import lombok.Value;

@Value
public class SolveResponse {

  @NonNull
  RecommendedAction recommendedAction;
}
