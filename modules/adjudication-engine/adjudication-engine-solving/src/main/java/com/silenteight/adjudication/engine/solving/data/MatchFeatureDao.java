package com.silenteight.adjudication.engine.solving.data;

import lombok.Value;

@Value
public class MatchFeatureDao {

  private final long alertId;
  private final long matchId;
  private final long agentConfigFeatureId;
  private final String feature;
  private final String agentConfig;
}
