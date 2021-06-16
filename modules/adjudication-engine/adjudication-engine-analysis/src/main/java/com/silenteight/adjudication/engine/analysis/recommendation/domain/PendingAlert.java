package com.silenteight.adjudication.engine.analysis.recommendation.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import com.silenteight.solving.api.v1.Alert;
import com.silenteight.solving.api.v1.FeatureVectorSolution;
import com.silenteight.solving.api.v1.Match;
import com.silenteight.solving.api.v1.MatchFlag;

import java.util.List;
import java.util.stream.Collectors;

@Value
@Setter(AccessLevel.NONE)
@Getter(AccessLevel.NONE)
public class PendingAlert {

  long alertId;

  List<FeatureVectorSolution> matchSolution;

  public Alert toAlert() {
    return Alert
        .newBuilder()
        .setName("alerts/" + alertId)
        .addAllMatches(
            matchSolution
                .stream()
                .map(m -> Match
                    .newBuilder()
                    .setSolution(m)
                    .addFlags(MatchFlag.MATCH_FLAG_UNSPECIFIED)
                    .build())
                .collect(
                    Collectors.toList()))
        .build();
  }
}
