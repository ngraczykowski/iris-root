package com.silenteight.fab.dataprep.domain;

import lombok.RequiredArgsConstructor;

import com.silenteight.fab.dataprep.domain.model.ExtractedAlert;
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
      Map<String, ExtractedAlert> extractedAlerts) {
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
      RegisteredAlertWithMatchesOut registeredAlertWithMatchesOut, ExtractedAlert extractedAlert) {
    List<Match> matches = registeredAlertWithMatchesOut
        .getRegisteredMatches()
        .stream()
        .map(registeredMatchOut -> convert(
            registeredMatchOut, extractedAlert.getMatch(registeredMatchOut.getMatchId())))
        .collect(toList());

    return RegisteredAlert
        .builder()
        .batchId(extractedAlert.getBatchId())
        .alertId(registeredAlertWithMatchesOut.getAlertId())
        .alertName(registeredAlertWithMatchesOut.getAlertName())
        .matches(matches)
        .build();
  }

  private static Match convert(
      RegisteredMatchOut registeredMatchOut, ExtractedAlert.Match match) {
    return Match
        .builder()
        .matchId(registeredMatchOut.getMatchId())
        .matchName(registeredMatchOut.getMatchName())
        .payload(match.getPayload())
        .build();
  }

  private static AlertWithMatchesIn convert(ExtractedAlert extractedAlert) {
    return AlertWithMatchesIn
        .builder()
        //.status()//TODO: Is it needed?
        //.errorDescription()//TODO: Is it needed?
        .alertId(extractedAlert.getAlertId())
        .matches(extractedAlert
            .getMatches()
            .values()
            .stream()
            .map(RegistrationService::convert)
            .collect(toList())).build();
  }

  private static MatchIn convert(ExtractedAlert.Match match) {
    return MatchIn.builder().matchId(match.getMatchId()).build();
  }
}
