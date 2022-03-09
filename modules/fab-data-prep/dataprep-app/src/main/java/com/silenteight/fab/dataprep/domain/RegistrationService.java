package com.silenteight.fab.dataprep.domain;

import lombok.RequiredArgsConstructor;

import com.silenteight.fab.dataprep.domain.model.ExtractedAlert;
import com.silenteight.fab.dataprep.domain.model.ExtractedAlert.Match;
import com.silenteight.registration.api.library.v1.*;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static java.util.function.UnaryOperator.identity;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@RequiredArgsConstructor
@Service
public class RegistrationService {

  private final RegistrationServiceClient registrationServiceClient;

  public void registerAlertsAndMatches(List<ExtractedAlert> extractedAlerts) {
    RegisterAlertsAndMatchesIn registerAlertsAndMatchesIn = RegisterAlertsAndMatchesIn.builder()
        .alertsWithMatches(
            extractedAlerts.stream().map(RegistrationService::convert).collect(toList())).build();
    Map<String, ExtractedAlert> alertMap = extractedAlerts.stream()
        .collect(toMap(ExtractedAlert::getAlertId, identity()));
    RegisterAlertsAndMatchesOut result =
        registrationServiceClient.registerAlertsAndMatches(registerAlertsAndMatchesIn);
    result
        .getRegisteredAlertWithMatches()
        .forEach(registered -> alertMap
            .get(registered.getAlertId())
            .setAlertName(registered.getAlertName()));
    //TODO we should add name to matches in the same way
  }

  private static AlertWithMatchesIn convert(ExtractedAlert extractedAlert) {
    return AlertWithMatchesIn
        .builder()
        //.status()//TODO: Is it needed?
        //.errorDescription()//TODO: Is it needed?
        .alertId(extractedAlert.getAlertId())
        .matches(extractedAlert
            .getMatches()
            .stream()
            .map(RegistrationService::convert)
            .collect(toList())).build();
  }

  private static MatchIn convert(Match match) {
    return MatchIn.builder().matchId(match.getMatchId()).build();
  }
}
