package com.silenteight.adjudication.engine.solving.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.solving.data.FeatureAggregate;

import java.io.Serial;
import java.io.Serializable;

@Getter
@RequiredArgsConstructor
public class MatchFeature implements Serializable {

  @Serial private static final long serialVersionUID = -3342537618800109906L;

  private final long alertId;
  private final long matchId;
  private final long agentConfigFeatureId;
  private final String feature;
  private final String agentConfig;
  private final String clientMatchId;
  private String featureValue;
  private String reason;

  public MatchFeature(
      long alertId, long matchId, String clientMatchId, FeatureAggregate featureAggregate) {
    this.alertId = alertId;
    this.matchId = matchId;
    agentConfigFeatureId = featureAggregate.agentConfigFeatureId();
    feature = featureAggregate.featureName();
    agentConfig = featureAggregate.agentConfig();
    this.clientMatchId = clientMatchId;
  }

  public static MatchFeature empty() {
    return new MatchFeature(0L, 0L, 0L, "", "", "");
  }

  public void updateFeatureValue(String featureValue, String reason) {
    this.featureValue = featureValue;
    this.reason = reason;
  }

  boolean hasValue() {
    return featureValue != null;
  }
}
