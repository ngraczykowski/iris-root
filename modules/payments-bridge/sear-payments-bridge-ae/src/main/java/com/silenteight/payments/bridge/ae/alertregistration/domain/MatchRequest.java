package com.silenteight.payments.bridge.ae.alertregistration.domain;

import lombok.Builder;
import lombok.Value;

import com.silenteight.adjudication.api.v1.BatchCreateAlertMatchesRequest;
import com.silenteight.adjudication.api.v1.BatchCreateMatchesRequest;
import com.silenteight.adjudication.api.v1.Match;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Value
@Builder
public class MatchRequest {

  String alertName;

  long[] matchId;

  public BatchCreateMatchesRequest toCreateMatchesRequest() {
    return BatchCreateMatchesRequest.newBuilder().addAllAlertMatches(List.of(
        BatchCreateAlertMatchesRequest
            .newBuilder()
            .addAllMatches(createMatches())
            .setAlert(alertName)
            .build())).build();
  }

  private List<Match> createMatches() {
    return Arrays
        .stream(matchId)
        .mapToObj(MatchRequest::createMatch)
        .collect(toList());
  }

  @NotNull
  private static Match createMatch(long matchId) {
    return Match.newBuilder().setMatchId(String.valueOf(matchId)).build();
  }
}
