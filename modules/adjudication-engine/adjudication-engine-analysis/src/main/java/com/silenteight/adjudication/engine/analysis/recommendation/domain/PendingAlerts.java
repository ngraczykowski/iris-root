package com.silenteight.adjudication.engine.analysis.recommendation.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Value;

import com.silenteight.solving.api.v1.BatchSolveAlertsRequest;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Value
@Getter(AccessLevel.NONE)
public class PendingAlerts {

  List<PendingAlert> pendingAlerts;

  public boolean isEmpty() {
    return pendingAlerts.isEmpty();
  }

  public int size() {
    return pendingAlerts.size();
  }

  public BatchSolveAlertsRequest toBatchSolveAlertsRequest(String strategy) {
    var requestAlerts = pendingAlerts.stream().map(PendingAlert::toAlert).collect(toList());

    return BatchSolveAlertsRequest
        .newBuilder()
        .setStrategy(strategy)
        .addAllAlerts(requestAlerts)
        .build();
  }
}
