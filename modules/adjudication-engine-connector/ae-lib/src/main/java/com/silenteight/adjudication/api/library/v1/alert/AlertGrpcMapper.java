package com.silenteight.adjudication.api.library.v1.alert;

import lombok.experimental.UtilityClass;

import com.silenteight.adjudication.api.v1.Alert;
import com.silenteight.adjudication.api.v1.BatchCreateAlertMatchesResponse;
import com.silenteight.adjudication.api.v1.BatchCreateAlertsResponse;
import com.silenteight.adjudication.api.v1.Match;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class AlertGrpcMapper {

  public Alert toAlert(String alertId, Integer alertPriority) {
    return Alert.newBuilder()
        .setAlertId(alertId)
        .setPriority(alertPriority)
        .build();
  }

  public BatchCreateAlertsOut toBatchCreateAlertsOut(BatchCreateAlertsResponse response) {
    return new BatchCreateAlertsOut(response.getAlertsList().stream()
        .map(AlertGrpcMapper::toAlertOut)
        .collect(Collectors.toList()));
  }

  public BatchCreateAlertMatchesOut toBatchCreateAlertMatchesOut(
      BatchCreateAlertMatchesResponse response) {

    return new BatchCreateAlertMatchesOut(response.getMatchesList().stream()
        .map(AlertGrpcMapper::toAlertMatchOut)
        .collect(Collectors.toList()));
  }

  public List<Match> toMatches(Collection<String> matchIds) {
    return matchIds.stream()
        .map(AlertGrpcMapper::toMatch)
        .collect(Collectors.toList());
  }

  public Match toMatch(String matchId) {
    return Match.newBuilder().setMatchId(matchId).build();
  }

  public List<AlertMatchOut> toAlertMatchesOut(BatchCreateAlertMatchesResponse response) {
    return response.getMatchesList().stream()
        .map(AlertGrpcMapper::toAlertMatchOut)
        .collect(Collectors.toList());
  }

  public AlertMatchOut toAlertMatchOut(Match match) {
    return AlertMatchOut.builder()
        .matchId(match.getMatchId())
        .name(match.getName())
        .build();
  }

  public AlertOut toAlertOut(Alert alert) {
    return AlertOut.builder()
        .alertId(alert.getAlertId())
        .name(alert.getName())
        .build();
  }
}
