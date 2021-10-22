package com.silenteight.adjudication.engine.analysis.recommendation.domain;

import lombok.*;

import com.silenteight.solving.api.v1.Alert;
import com.silenteight.solving.api.v1.FeatureVectorSolution;
import com.silenteight.solving.api.v1.Match;
import com.silenteight.solving.api.v1.MatchFlag;

import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;
import java.util.stream.Collectors;

import static lombok.AccessLevel.PACKAGE;

@Value
@Builder
@Setter(AccessLevel.NONE)
@Getter(AccessLevel.NONE)
public class PendingAlert {

  @Getter(PACKAGE)
  long alertId;

  List<FeatureVectorSolution> matchSolutions;

  @Getter
  long[] matchIds;

  @Getter
  ObjectNode[] matchContexts;

  public Alert toAlert() {
    var matches = matchSolutions.stream()
        .map(m -> Match.newBuilder()
            .setSolution(m)
            .addFlags(MatchFlag.MATCH_FLAG_UNSPECIFIED)
            .build())
        .collect(Collectors.toList());

    return Alert
        .newBuilder()
        .setName("alerts/" + alertId)
        .addAllMatches(matches)
        .build();
  }
}
