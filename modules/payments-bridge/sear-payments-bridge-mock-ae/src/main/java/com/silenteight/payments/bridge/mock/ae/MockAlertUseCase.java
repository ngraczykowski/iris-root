package com.silenteight.payments.bridge.mock.ae;

import com.silenteight.adjudication.api.v1.Alert;
import com.silenteight.adjudication.api.v1.BatchCreateAlertMatchesRequest;
import com.silenteight.adjudication.api.v1.BatchCreateAlertMatchesResponse;
import com.silenteight.adjudication.api.v1.Match;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Set;

import static java.util.stream.Collectors.toList;

@Service
@Profile("mockae")
public class MockAlertUseCase {

  private static long alertId = 1;
  private static int matchId = 1;

  static Alert createAlert(Alert alert, Set<Long> existing) {
    while(existing.contains(alertId)) {
      alertId++;
    }

    var response =
        Alert.newBuilder().setAlertId(alert.getAlertId()).setName("alerts/" + alertId).build();
    return response;
  }

  static BatchCreateAlertMatchesResponse batchCreateAlertMatches(
      BatchCreateAlertMatchesRequest request) {
    var response = BatchCreateAlertMatchesResponse
        .newBuilder()
        .addAllMatches(request
            .getMatchesList()
            .stream()
            .map(m -> Match
                .newBuilder()
                .setMatchId(m.getMatchId())
                .setName("alerts/" + alertId + "/matches/" + matchId)
                .build())
            .collect(
                toList()))
        .build();
    matchId++;
    return response;
  }

  public static long getCreatedAlertsCount() {
    return alertId;
  }

  public static int getCreatedMatchesCount() {
    return matchId;
  }
}
