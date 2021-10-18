package com.silenteight.payments.bridge.ae.alertregistration.domain;

import lombok.Builder;
import lombok.Value;

import com.silenteight.adjudication.api.v1.Alert;
import com.silenteight.adjudication.api.v1.BatchCreateAlertMatchesRequest;
import com.silenteight.adjudication.api.v1.CreateAlertRequest;
import com.silenteight.adjudication.api.v1.Match;

import com.google.protobuf.Timestamp;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;

import static java.util.stream.Collectors.toList;

@Value
@Builder
public class RegisterAlertRequest {

  String alertId;

  int priority;

  Timestamp alertTime;

  List<String> matchIds;

  MatchQuantity matchQuantity;

  List<Label> labels;

  public BatchCreateAlertMatchesRequest toCreateMatchesRequest(String alertName) {
    if (matchIds.isEmpty())
      throw new IllegalStateException("Match ids must not be empty");

    return BatchCreateAlertMatchesRequest
        .newBuilder()
        .addAllMatches(createMatches())
        .setAlert(alertName)
        .build();
  }

  private List<Match> createMatches() {
    return matchIds.stream()
        .map(RegisterAlertRequest::createMatch)
        .collect(toList());
  }

  @NotNull
  private static Match createMatch(String matchId) {
    return Match.newBuilder().setMatchId(matchId).build();
  }

  public CreateAlertRequest toCreateAlertRequest() {
    var alert = Alert
        .newBuilder()
        .setAlertId(getAlertId())
        .setPriority(getPriority());
    if (alertTime != null) {
      alert.setAlertTime(alertTime);
    }
    if (matchQuantity != null) {
      alert.putLabels("matchQuantity", matchQuantity.name().toLowerCase(Locale.ROOT));
    }

    if (labels != null) {
      labels.forEach(l -> alert.putLabels(l.getName(), l.getValue()));
    }

    return CreateAlertRequest.newBuilder().setAlert(alert.build()).build();
  }
}
