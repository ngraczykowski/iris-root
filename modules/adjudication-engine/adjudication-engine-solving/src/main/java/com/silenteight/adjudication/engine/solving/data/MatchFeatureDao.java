package com.silenteight.adjudication.engine.solving.data;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class MatchFeatureDao {

  long analysisId;
  long alertId;
  long matchId;
  long agentConfigFeatureId;
  String feature;
  String agentConfig;
  String policy;
  String strategy;
  String featureValue;
  String featureReason;
  String clientMatchId;
  String category;
  String categoryValue;
}
