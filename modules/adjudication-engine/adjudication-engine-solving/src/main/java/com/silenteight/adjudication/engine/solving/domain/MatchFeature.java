package com.silenteight.adjudication.engine.solving.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.solving.data.MatchFeatureDao;

@Getter
@RequiredArgsConstructor
public class MatchFeature {

  private final long alertId;
  private final long matchId;
  private final long agentConfigFeatureId;
  private final String feature;
  private final String agentConfig;

  public static final MatchFeature from(MatchFeatureDao dao) {
    // TODO: I'm note sure if we should replace . ian / inside agent config to send via rabbit
    return new MatchFeature(dao.getAlertId(), dao.getMatchId(), dao.getAgentConfigFeatureId(),
        dao.getFeature(), dao.getAgentConfig());
  }

}
