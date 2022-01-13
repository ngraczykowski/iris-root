package com.silenteight.adjudication.api.library.v1.alert;

import lombok.experimental.UtilityClass;

import com.silenteight.adjudication.api.v1.Alert;
import com.silenteight.adjudication.api.v1.BatchCreateAlertMatchesResponse;
import com.silenteight.adjudication.api.v1.BatchCreateAlertsResponse;
import com.silenteight.adjudication.api.v1.Match;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
class AlertGrpcMapper {

  Alert mapToAlert(String alert) {
    return Alert.newBuilder().setAlertId(alert).build();
  }

  BatchCreateAlertsOut mapToBatchCreateAlertsOut(BatchCreateAlertsResponse response) {
    return new BatchCreateAlertsOut(mapToAlertOuts(response.getAlertsList()));
  }

  BatchCreateAlertMatchesOut mapToBatchCreateAlertMatchesOut(
      BatchCreateAlertMatchesResponse response) {
    return new BatchCreateAlertMatchesOut(mapToAlertMatches(response.getMatchesList()));
  }

  List<AlertMatchOut> mapToAlertMatches(List<Match> matches) {
    return matches.stream()
        .map(AlertGrpcMapper::mapToAlertMatchOut)
        .collect(Collectors.toList());
  }

  Match mapToMatch(String matchId) {
    return Match.newBuilder().setMatchId(matchId).build();
  }

  AlertWithMatchesOut mapToAlertWithMatches(
      BatchCreateAlertMatchesIn alert, String alertName, List<AlertMatchOut> matches) {
    return AlertWithMatchesOut.builder()
        .alertId(alert.getAlertId())
        .alertName(alertName)
        .matches(matches)
        .build();
  }

  private List<AlertOut> mapToAlertOuts(List<Alert> alerts) {
    return alerts.stream()
        .map(AlertGrpcMapper::mapToAlertOut)
        .collect(Collectors.toList());
  }

  private AlertOut mapToAlertOut(Alert alert) {
    return AlertOut.builder()
        .alertId(alert.getAlertId())
        .name(alert.getName())
        .build();
  }

  private AlertMatchOut mapToAlertMatchOut(Match match) {
    return AlertMatchOut.builder()
        .matchId(match.getMatchId())
        .name(match.getName())
        .build();
  }
}
