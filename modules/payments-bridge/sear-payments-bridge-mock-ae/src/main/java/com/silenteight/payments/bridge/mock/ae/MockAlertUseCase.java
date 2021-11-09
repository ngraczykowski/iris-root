package com.silenteight.payments.bridge.mock.ae;

import com.silenteight.adjudication.api.v1.*;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Service
@Profile("mockae")
public class MockAlertUseCase {

  private static long alertId = 1;
  private static int matchId = 1;

  private static final List<Alert> ALERTS = new ArrayList<>();

  static Alert createAlert(Alert alert, Set<Long> existing) {
    alertId++;
    var savedAlert =
        Alert.newBuilder().setAlertId(alert.getAlertId()).setName("alerts/" + alertId).build();
    ALERTS.add(savedAlert);
    return savedAlert;
  }

  static BatchCreateMatchesResponse batchCreateMatches(BatchCreateMatchesRequest request) {
    return BatchCreateMatchesResponse.newBuilder().addAllMatches(request
        .getAlertMatchesList()
        .stream()
        .map(MockAlertUseCase::batchCreateAlertMatches)
        .map(BatchCreateAlertMatchesResponse::getMatchesList)
        .flatMap(List::stream)
        .collect(toList())).build();
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
    return ALERTS.size();
  }

  public static int getCreatedMatchesCount() {
    return matchId - 1;
  }
}
