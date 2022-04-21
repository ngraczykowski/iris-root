package com.silenteight.adjudication.engine.solving.data;

import lombok.Value;

@Value
public class MatchFeatureDao {

  long analysisId;
  long alertId;
  long matchId;
  long agentConfigFeatureId;
  String feature;
  String agentConfig;
  String policy;
  String strategy;
}
