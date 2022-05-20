package com.silenteight.adjudication.engine.solving.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.solving.data.MatchFeatureDao;

import java.io.Serializable;

@Getter
@RequiredArgsConstructor
public class MatchFeature implements Serializable {

  private static final long serialVersionUID = -3342537618800109906L;
  private final long alertId;
  private final long matchId;
  private final long agentConfigFeatureId;
  private final String feature;
  private final String agentConfig;
  private final String clientMatchId;
  private String featureValue;
  private String reason;

  public static MatchFeature from(MatchFeatureDao dao) {
    // TODO: I'm note sure if we should replace . ian / inside agent config to send via rabbit
    var match = new MatchFeature(dao.getAlertId(), dao.getMatchId(), dao.getAgentConfigFeatureId(),
        dao.getFeature(), dao.getAgentConfig(), dao.getClientMatchId());

    match.updateFeatureValue(dao.getFeatureValue(), dao.getFeatureReason());

    return match;
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
