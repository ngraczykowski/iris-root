package com.silenteight.payments.bridge.ae.alertregistration.domain;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Value
@Builder
public class RegisterAlertResponse {

  String systemId;
  String alertName;

  List<RegisterMatchResponse> matchResponses;

  public Map<String, String> getMatchResponsesAsMap() {
    return matchResponses.stream().collect(Collectors.toMap(
        RegisterMatchResponse::getMatchId,
        RegisterMatchResponse::getMatchName));
  }

  @NonNull
  public SaveRegisteredAlertRequest toSaveRegisterAlertRequest(
      List<RegisterAlertRequest> registerAlertRequests) {
    var alertMessageId = registerAlertRequests
        .stream()
        .filter(rar -> rar.getFkcoSystemId().equals(systemId))
        .findFirst()
        .orElseThrow(() -> new NoSuchElementException(
            "There is no corresponding alert in registerAlertRequest for RegisterAlertResponse"))
        .getAlertMessageId();

    return SaveRegisteredAlertRequest
        .builder()
        .alertMessageId(alertMessageId)
        .alertName(alertName)
        .fkcoSystemId(systemId)
        .matches(matchResponses
            .stream()
            .map(RegisterMatchResponse::toSaveRegisteredMatchRequest)
            .collect(toList()))
        .build();
  }

  public String getMatchName(String hitId) {
    return matchResponses
        .stream()
        .filter(m -> m.getMatchId().equals(hitId))
        .findFirst()
        .orElseThrow(() -> new NoSuchElementException("There is no corresponding match"))
        .getMatchName();
  }
}
