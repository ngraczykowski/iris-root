package com.silenteight.payments.bridge.ae.alertregistration.domain;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import com.silenteight.payments.bridge.common.event.LearningAlertRegisteredEvent;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Value
@Builder
public class RegisterAlertResponse {

  String alertMessageId;
  String alertName;

  List<RegisterMatchResponse> matchResponses;

  public Map<String, String> getMatchResponsesAsMap() {
    return matchResponses.stream().collect(Collectors.toMap(
        RegisterMatchResponse::getMatchId,
        RegisterMatchResponse::getMatchName));
  }

  public LearningAlertRegisteredEvent toLearningAlertRegisteredEvent() {
    return LearningAlertRegisteredEvent
        .builder()
        .alertName(alertName)
        .alertMessageId(alertMessageId)
        .build();
  }

  @NonNull
  public SaveRegisteredAlertRequest toSaveRegisterAlertRequest(
      List<RegisterAlertRequest> registerAlertRequests) {
    var fkcoSystemId = registerAlertRequests
        .stream()
        .filter(rar -> rar.alertMessageIdAsString().equals(alertMessageId))
        .findFirst()
        .orElseThrow(() -> new NoSuchElementException(
            "There is no corresponding alert in registerAlertRequest for RegisterAlertResponse"))
        .getFkcoSystemId();

    return SaveRegisteredAlertRequest
        .builder()
        .alertMessageId(UUID.fromString(alertMessageId))
        .alertName(alertName)
        .fkcoSystemId(fkcoSystemId)
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
