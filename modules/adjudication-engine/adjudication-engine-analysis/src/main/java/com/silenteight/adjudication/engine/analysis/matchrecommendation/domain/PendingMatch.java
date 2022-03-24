package com.silenteight.adjudication.engine.analysis.matchrecommendation.domain;

import lombok.Builder;
import lombok.Value;

import com.silenteight.solving.api.v1.Alert;
import com.silenteight.solving.api.v1.FeatureVectorSolution;
import com.silenteight.solving.api.v1.Match;
import com.silenteight.solving.api.v1.MatchFlag;

import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;

@Value
@Builder
public class PendingMatch {

  long alertId;

  long matchId;

  FeatureVectorSolution matchSolution;

  ObjectNode matchContexts;

  public Alert toAlert() {
    var match = Match
        .newBuilder()
        .setSolution(matchSolution)
        .addFlags(MatchFlag.MATCH_FLAG_UNSPECIFIED)
        .build();

    return Alert
        .newBuilder()
        .setName("alerts/" + alertId + "/matches/" + matchId)
        .addAllMatches(List.of(match))
        .build();
  }

}
