package com.silenteight.payments.bridge.ae.alertregistration.domain;

import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Value
@Builder
public class RegisterAlertResponse {

  String alertId;
  String alertName;

  List<RegisterMatchResponse> matchResponses;

  public Map<String, String> getMatchResponsesAsMap() {
    return matchResponses.stream().collect(Collectors.toMap(
        RegisterMatchResponse::getMatchId,
        RegisterMatchResponse::getMatchName));
  }

}
