package com.silenteight.serp.governance.policy.solve.event;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.silenteight.serp.governance.policy.solve.event.FeatureVectorEventStrategy.SOLVE;

@RequiredArgsConstructor
@Slf4j
public class FeatureVectorEventStrategyService {

  @NonNull
  private final FeatureVectorEventStrategy strategy;

  public boolean isSolve() {
    return strategy == SOLVE;
  }
}
