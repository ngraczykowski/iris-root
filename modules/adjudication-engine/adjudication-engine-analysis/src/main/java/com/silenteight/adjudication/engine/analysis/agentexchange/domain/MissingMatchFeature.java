package com.silenteight.adjudication.engine.analysis.agentexchange.domain;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class MissingMatchFeature {

  long alertId;

  long matchId;

  long agentConfigFeatureId;

  @NonNull
  String agentConfig;

  @NonNull
  String feature;

  int priority;

  public boolean isValid() {
    return alertId != 0 && matchId != 0 && !agentConfig.isEmpty() && !feature.isEmpty();
  }
}
