package com.silenteight.adjudication.engine.solving.application.publisher.dto;

import lombok.AllArgsConstructor;
import lombok.Value;

import com.silenteight.solving.api.v1.*;

import java.io.Serializable;
import java.util.List;

import static java.util.stream.Collectors.toList;

@AllArgsConstructor
@Value
public class AlertSolutionRequest implements Serializable {

  //TODO: I've never seen a different strategy (Checked in all dbs too). Verify if that is true.
  private static final String STRATEGY = "strategies/USE_ANALYST_SOLUTION";

  String alertName;

  List<String> solutions;

  long alertId;

  public BatchSolveAlertsRequest mapToBatchSolveAlertsRequest() {
    return BatchSolveAlertsRequest.newBuilder()
        .setStrategy(STRATEGY)
        .addAlerts(createAlert())
        .build();
  }

  private Alert createAlert() {
    return Alert.newBuilder()
        .setName(alertName)
        .addAllMatches(createMatches())
        .build();
  }

  private List<Match> createMatches() {
    return solutions.stream()
        .map(AlertSolutionRequest::createMatch)
        .collect(toList());
  }

  private static Match createMatch(String solution) {
    return Match.newBuilder()
        .setSolution(createSolution(solution))
        // TODO: I see that only this flag is used. Verify it
        .addFlags(MatchFlag.MATCH_FLAG_UNSPECIFIED)
        .build();
  }

  private static FeatureVectorSolution createSolution(String solution) {
    return FeatureVectorSolution.valueOf(solution);
  }
}
