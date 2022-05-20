package com.silenteight.adjudication.engine.analysis.recommendation.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Value;

import com.silenteight.solving.api.v1.BatchSolveAlertsRequest;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Value
@Getter(AccessLevel.NONE)
public class PendingAlerts {

  LinkedHashMap<Long, PendingAlert> pendingAlertsMap;

  public PendingAlerts(List<PendingAlert> pendingAlerts) {
    pendingAlertsMap = new LinkedHashMap<>();
    pendingAlerts.forEach(p -> pendingAlertsMap.put(p.getAlertId(), p));
  }

  public boolean isEmpty() {
    return pendingAlertsMap.isEmpty();
  }

  public int size() {
    return pendingAlertsMap.size();
  }

  public BatchSolveAlertsRequest toBatchSolveAlertsRequest(String strategy) {
    var requestAlerts = pendingAlertsMap
        .values()
        .stream()
        .map(PendingAlert::toAlert)
        .collect(toList());

    return BatchSolveAlertsRequest
        .newBuilder()
        .setStrategy(strategy)
        .addAllAlerts(requestAlerts)
        .build();
  }

  public Optional<PendingAlert> getById(long alertId) {
    return Optional.ofNullable(pendingAlertsMap.get(alertId));
  }
}
