package com.silenteight.serp.governance.strategy.solve;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.solving.api.v1.Alert;
import com.silenteight.solving.api.v1.FeatureVectorSolution;
import com.silenteight.solving.api.v1.Match;
import com.silenteight.solving.api.v1.MatchFlag;

import static com.silenteight.solving.api.v1.MatchFlag.MATCH_FLAG_UNSPECIFIED;
import static com.silenteight.solving.api.v1.MatchFlag.OBSOLETE;
import static com.silenteight.solving.api.v1.MatchFlag.SOLVED;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class AlertTestUtils {

  static Alert alert(String name, Match... matches) {
    return Alert.newBuilder()
        .setName(name)
        .addAllMatches(asList(matches))
        .build();
  }

  static Match newMatch(FeatureVectorSolution solution) {
    return match(solution, MATCH_FLAG_UNSPECIFIED);
  }

  static Match obsoleteMatch(FeatureVectorSolution solution) {
    return match(solution, OBSOLETE);
  }

  static Match solvedMatch(FeatureVectorSolution solution) {
    return match(solution, SOLVED);
  }

  private static Match match(FeatureVectorSolution solution, MatchFlag flag) {
    return Match.newBuilder()
        .setSolution(solution)
        .addAllFlags(singletonList(flag))
        .build();
  }
}
