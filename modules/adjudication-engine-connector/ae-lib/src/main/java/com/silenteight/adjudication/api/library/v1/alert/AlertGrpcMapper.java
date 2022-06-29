package com.silenteight.adjudication.api.library.v1.alert;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

import com.silenteight.adjudication.api.library.v1.util.TimeStampUtil;
import com.silenteight.adjudication.api.v1.Alert;
import com.silenteight.adjudication.api.v1.BatchCreateAlertsResponse;
import com.silenteight.adjudication.api.v1.Match;

import com.google.protobuf.Timestamp;
import com.google.protobuf.TimestampOrBuilder;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
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
        .putAllLabels(batchRegisterAlertMatchesIn.getLabels())
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
                .labels(alert.getLabelsMap())
                .alertTime(toOffsetDateTime(alert.getAlertTimeOrBuilder()))
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

  private static Instant toInstant(@NonNull TimestampOrBuilder timestamp) {
    return Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos());
  }

  private static OffsetDateTime toOffsetDateTime(@NonNull TimestampOrBuilder timestamp) {
    Instant instant = toInstant(timestamp);
    return OffsetDateTime.ofInstant(instant, ZoneId.systemDefault());
  }
}
