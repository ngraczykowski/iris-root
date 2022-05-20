package com.silenteight.adjudication.engine.analysis.categoryrequest.domain;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class MatchAlert {

  long alertId;

  long matchId;

  public String getName() {
    return "alerts/" + alertId + "/matches/" + matchId;
  }
}
