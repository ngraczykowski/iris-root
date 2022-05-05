package com.silenteight.adjudication.api.library.v1.alert;

import lombok.experimental.UtilityClass;

import com.silenteight.adjudication.api.library.v1.util.TimeStampUtil;
import com.silenteight.adjudication.api.v1.Alert;
import com.silenteight.adjudication.api.v1.BatchCreateAlertsResponse;
import com.silenteight.adjudication.api.v1.Match;

import com.google.protobuf.Timestamp;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@UtilityClass
class AlertGrpcMapper {

  public Alert toAlert(BatchRegisterAlertMatchesIn batchRegisterAlertMatchesIn) {
    return Alert.newBuilder()
        .setAlertId(batchRegisterAlertMatchesIn.getAlertId())
        .setPriority(batchRegisterAlertMatchesIn.getAlertPriority())
        .mergeAlertTime(toTimestamp(batchRegisterAlertMatchesIn.getAlertTime()))
        .addAllMatches(toMatches(batchRegisterAlertMatchesIn.getMatchIds()))
        .build();
  }

  public RegisterAlertsAndMatchesOut toRegisterAlertsAndMatchesOut(
      BatchCreateAlertsResponse batchCreateAlertsResponse) {
    return RegisterAlertsAndMatchesOut.builder()
        .alertWithMatches(batchCreateAlertsResponse.getAlertsList().stream()
            .map(alert -> AlertWithMatchesOut.builder()
                .alertId(alert.getAlertId())
                .alertName(alert.getName())
                .matches(AlertGrpcMapper.toAlertMatchesOut(alert.getMatchesList()))
                .build())
            .collect(Collectors.toList()))
        .build();
  }

  public List<Match> toMatches(Collection<String> matchIds) {
    return matchIds.stream()
        .map(AlertGrpcMapper::toMatch)
        .collect(Collectors.toList());
  }

  private Match toMatch(String matchId) {
    return Match.newBuilder()
        .setMatchId(matchId)
        .build();
  }

  private List<AlertMatchOut> toAlertMatchesOut(List<Match> matches) {
    return matches.stream()
        .map(AlertGrpcMapper::toAlertMatchOut)
        .collect(Collectors.toList());
  }

  private AlertMatchOut toAlertMatchOut(Match match) {
    return AlertMatchOut.builder()
        .matchId(match.getMatchId())
        .name(match.getName())
        .build();
  }

  private Timestamp toTimestamp(OffsetDateTime time) {
    return Optional.ofNullable(time)
        .map(TimeStampUtil::fromOffsetDateTime)
        .orElse(null);
  }
}
