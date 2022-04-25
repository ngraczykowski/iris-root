package com.silenteight.scb.ingest.adapter.incomming.common.util;

import lombok.experimental.UtilityClass;

import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert;
import com.silenteight.scb.ingest.adapter.incomming.common.model.match.Match;
import com.silenteight.scb.ingest.domain.model.RegistrationResponse;
import com.silenteight.scb.ingest.domain.model.RegistrationResponse.RegisteredAlertWithMatches;
import com.silenteight.scb.ingest.domain.model.RegistrationResponse.RegisteredMatch;

import java.util.List;

@UtilityClass
public class AlertUpdater {

  public void updateWithRegistrationResponse(
      List<Alert> alerts,
      RegistrationResponse registrationResponse) {
    alerts.forEach(alert -> updateWithRegistrationResponse(alert, registrationResponse));
  }

  public void updateWithRegistrationResponse(Alert alert,
      RegistrationResponse registrationResponse) {
    var rawm = requireRegisteredAlertWithMatches(alert, registrationResponse);
    alert.details().setAlertName(rawm.getAlertName());

    alert.matches().forEach(match ->
        match.details().setMatchName(requireRegisteredMatch(match, rawm).getMatchName()));
  }

  private RegisteredAlertWithMatches requireRegisteredAlertWithMatches(
      Alert alert, RegistrationResponse registrationResponse) {
    return registrationResponse.getRegisteredAlertWithMatches()
        .stream()
        .filter(awm -> awm.getAlertId().equals(alert.id().sourceId()))
        .findFirst()
        .orElseThrow(
            () -> new IllegalStateException(
                "Missing alert with id: " + alert.id().sourceId() + " in registration response"));
  }

  private RegisteredMatch requireRegisteredMatch(
      Match match, RegisteredAlertWithMatches registeredAlertWithMatches) {
    return registeredAlertWithMatches.getRegisteredMatches()
        .stream()
        .filter(registeredMatch -> match.id().sourceId().equals(registeredMatch.getMatchId()))
        .findFirst()
        .orElseThrow(() -> new IllegalStateException(
            "Missing match with id: " + match.id().sourceId() + " in registration response"));
  }

}
