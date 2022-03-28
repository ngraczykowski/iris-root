package com.silenteight.scb.ingest.adapter.incomming.common.util;

import lombok.experimental.UtilityClass;

import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert;
import com.silenteight.scb.ingest.adapter.incomming.common.model.match.Match;
import com.silenteight.scb.ingest.domain.model.RegistrationResponse;
import com.silenteight.scb.ingest.domain.model.RegistrationResponse.RegisteredAlertWithMatches;
import com.silenteight.scb.ingest.domain.model.RegistrationResponse.RegisteredMatch;

import java.util.Collection;
import java.util.List;

@UtilityClass
public class AlertUpdater {

  public void updatedWithRegistrationInfo(Alert alert, RegistrationResponse registrationResponse) {
    registrationResponse.getRegisteredAlertWithMatches().stream()
        .peek(registeredAlertWithMatches -> alert
            .details()
            .setAlertName(registeredAlertWithMatches.getAlertName()))
        .map(RegisteredAlertWithMatches::getRegisteredMatches)
        .flatMap(Collection::stream)
        .forEach(
            registeredMatch -> updateMatchWithRegistrationInfo(alert.matches(), registeredMatch));
  }

  private void updateMatchWithRegistrationInfo(
      List<Match> matches, RegisteredMatch registeredMatch) {
    matches.stream()
        .filter(match -> match.id().sourceId().equals(registeredMatch.getMatchId()))
        .findFirst()
        .ifPresent(match -> match.details().setMatchName(registeredMatch.getMatchName()));
  }
}
