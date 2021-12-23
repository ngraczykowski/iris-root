package com.silenteight.payments.bridge.ae.alertregistration.domain;

import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

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

  public SaveRegisteredAlertRequest toSaveRegisterAlertRequest() {
    return SaveRegisteredAlertRequest
        .builder()
        .alertId(UUID.fromString(alertId))
        .alertName(alertName)
        .matches(matchResponses
            .stream()
            .map(RegisterMatchResponse::toSaveRegisteredMatchRequest)
            .collect(toList()))
        .build();
  }
}
