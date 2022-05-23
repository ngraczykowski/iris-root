package com.silenteight.payments.bridge.mock.ae;

import com.silenteight.adjudication.api.v1.*;
import com.silenteight.payments.bridge.common.resource.ResourceName;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

@Service
@Profile("mockae")
public class MockAlertUseCase {

  private static int matchId = 1;

  private static final Map<Long, Alert> ALERTS = new HashMap<>();

  static Alert getOrCreate(Alert alert, Function<String, Long> function) {
    var alertStored = ALERTS
        .values()
        .stream()
        .filter(a -> alert.getAlertId().equals(a.getAlertId()))
        .findFirst();
    if (alertStored.isPresent()) {
      return alertStored.get();
    }
    Long databaseId = function.apply(alert.getAlertId());
    return cacheAlert(databaseId, alert);
  }

  public static Alert cacheAlert(Long aeDatabaeId, Alert alert) {
    var storedAlert = Alert
        .newBuilder()
        .setAlertId(alert.getAlertId())
        .setPriority(alert.getPriority())
        .putAllLabels(alert.getLabelsMap())
        .setName("alerts/" + aeDatabaeId)
        .build();
    ALERTS.put(aeDatabaeId, storedAlert);
    return storedAlert;
  }

  static Alert getCacheAlert(Long id) {
    return ALERTS.get(id);
  }

  static BatchCreateMatchesResponse batchCreateMatches(BatchCreateMatchesRequest request) {
    return BatchCreateMatchesResponse
        .newBuilder()
        .addAllMatches(request
            .getAlertMatchesList()
            .stream()
            .map(MockAlertUseCase::batchCreateAlertMatches)
            .map(BatchCreateAlertMatchesResponse::getMatchesList)
            .flatMap(List::stream)
            .collect(toList()))
        .build();
  }

  static BatchCreateAlertMatchesResponse batchCreateAlertMatches(
      BatchCreateAlertMatchesRequest request) {
    Long requestedAlertId = ResourceName.create(request.getAlert()).getLong("alerts");
    var response = BatchCreateAlertMatchesResponse
        .newBuilder()
        .addAllMatches(request
            .getMatchesList()
            .stream()
            .map(m -> Match
                .newBuilder()
                .setMatchId(m.getMatchId())
                .setName("alerts/" + requestedAlertId + "/matches/" + matchId)
                .build())
            .collect(toList()))
        .build();
    matchId++;
    return response;
  }

  static BatchAddLabelsResponse batchAddAlertsResponse(BatchAddLabelsRequest request) {
    return BatchAddLabelsResponse.newBuilder().putAllLabels(request.getLabelsMap()).build();
  }

  public static boolean containsAlertId(String alertId) {
    return ALERTS.values().stream().anyMatch(a -> a.getAlertId().equals(alertId));
  }

  public static String getAlertName(String alertId) {
    return ALERTS
        .values()
        .stream()
        .filter(a -> a.getAlertId().equals(alertId))
        .findFirst()
        .get()
        .getName();
  }
}
