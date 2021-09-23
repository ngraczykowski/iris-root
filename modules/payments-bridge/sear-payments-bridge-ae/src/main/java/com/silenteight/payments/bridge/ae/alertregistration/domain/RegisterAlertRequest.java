package com.silenteight.payments.bridge.ae.alertregistration.domain;

import lombok.Builder;
import lombok.Value;

import com.silenteight.adjudication.api.v1.Alert;
import com.silenteight.adjudication.api.v1.BatchCreateAlertMatchesRequest;
import com.silenteight.adjudication.api.v1.Match;

import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import static com.silenteight.payments.bridge.common.protobuf.TimestampConverter.fromSqlTimestamp;
import static java.util.stream.Collectors.toList;

@Value
@Builder
public class RegisterAlertRequest {

  long alertId;

  Timestamp createTime;

  int priority;

  long[] matchId;

  public BatchCreateAlertMatchesRequest toCreateMatchesRequest(String alertName) {
    return BatchCreateAlertMatchesRequest
            .newBuilder()
            .addAllMatches(createMatches())
            .setAlert(alertName)
            .build();
  }

  private List<Match> createMatches() {
    return Arrays
        .stream(matchId)
        .mapToObj(RegisterAlertRequest::createMatch)
        .collect(toList());
  }

  @NotNull
  private static Match createMatch(long matchId) {
    return Match.newBuilder().setMatchId(String.valueOf(matchId)).build();
  }

  public Alert toCreateAlertRequest() {
    return Alert
        .newBuilder()
        .setAlertId(String.valueOf(getAlertId()))
        .setPriority(getPriority())
        .setCreateTime(fromSqlTimestamp(getCreateTime()))
        .build();
  }
}
