package com.silenteight.fab.dataprep.domain;

import lombok.RequiredArgsConstructor;

import com.silenteight.fab.dataprep.domain.model.ParsedAlertMessage;
import com.silenteight.fab.dataprep.domain.model.ParsedAlertMessage.Hit;
import com.silenteight.fab.dataprep.domain.model.RegisteredAlert;
import com.silenteight.fab.dataprep.domain.model.RegisteredAlert.Match;
import com.silenteight.registration.api.library.v1.*;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Service
public class RegistrationService {

  private final RegistrationServiceClient registrationServiceClient;

  public List<RegisteredAlert> registerAlertsAndMatches(
      Map<String, ParsedAlertMessage> extractedAlerts) {
    RegisterAlertsAndMatchesIn registerAlertsAndMatchesIn = RegisterAlertsAndMatchesIn
        .builder()
        .alertsWithMatches(
            extractedAlerts.values().stream().map(RegistrationService::convert).collect(toList()))
        .build();
    RegisterAlertsAndMatchesOut result =
        registrationServiceClient.registerAlertsAndMatches(registerAlertsAndMatchesIn);

    return result
        .getRegisteredAlertWithMatches()
        .stream()
        .map(registeredAlertWithMatchesOut -> convert(
            registeredAlertWithMatchesOut,
            extractedAlerts.get(registeredAlertWithMatchesOut.getAlertId())))
        .collect(toList());
  }

  private static RegisteredAlert convert(
      RegisteredAlertWithMatchesOut registeredAlertWithMatchesOut,
      ParsedAlertMessage parsedAlertMessage) {
    List<Match> matches = registeredAlertWithMatchesOut
        .getRegisteredMatches()
        .stream()
        .map(registeredMatchOut -> convert(
            registeredMatchOut, parsedAlertMessage.getHit(registeredMatchOut.getMatchId())))
        .collect(toList());

    return RegisteredAlert
        .builder()
        .batchName(parsedAlertMessage.getBatchName())
        .messageName(registeredAlertWithMatchesOut.getAlertId())
        .alertName(registeredAlertWithMatchesOut.getAlertName())
        .matches(matches)
        .build();
  }

  private static Match convert(RegisteredMatchOut registeredMatchOut, Hit hit) {
    return Match
        .builder()
        .hitName(registeredMatchOut.getMatchId())
        .matchName(registeredMatchOut.getMatchName())
        .payload(hit.getPayload())
        .build();
  }

  private static AlertWithMatchesIn convert(ParsedAlertMessage parsedAlertMessage) {
    return AlertWithMatchesIn
        .builder()
        //.status()//TODO: Is it needed?
        //.errorDescription()//TODO: Is it needed?
        .alertId(parsedAlertMessage.getMessageName())
        .matches(parsedAlertMessage
            .getHits()
            .values()
            .stream()
            .map(RegistrationService::convert)
            .collect(toList())).build();
  }

  private static MatchIn convert(Hit hit) {
    return MatchIn.builder().matchId(hit.getHitName()).build();
  }
}
