package com.silenteight.fab.dataprep.domain;

import lombok.RequiredArgsConstructor;

import com.silenteight.fab.dataprep.domain.model.ParsedAlertMessage;
import com.silenteight.fab.dataprep.domain.model.ParsedAlertMessage.Hit;
import com.silenteight.fab.dataprep.domain.model.RegisteredAlert;
import com.silenteight.fab.dataprep.domain.model.RegisteredAlert.Match;
import com.silenteight.registration.api.library.v1.*;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Service
public class RegistrationService {

  private final RegistrationServiceClient registrationServiceClient;

  public List<RegisteredAlert> registerAlertsAndMatches(
      Map<String, ParsedAlertMessage> parsedAlertMessagesMap) {
    Collection<ParsedAlertMessage> parsedAlertMessages = parsedAlertMessagesMap.values();
    RegisterAlertsAndMatchesIn registerAlertsAndMatchesIn = RegisterAlertsAndMatchesIn
        .builder()
        .batchId(getBatchName(parsedAlertMessages))
        .alertsWithMatches(
            parsedAlertMessages
                .stream()
                .map(RegistrationService::convert)
                .collect(toList()))
        .build();
    RegisterAlertsAndMatchesOut result =
        registrationServiceClient.registerAlertsAndMatches(registerAlertsAndMatchesIn);

    return result
        .getRegisteredAlertWithMatches()
        .stream()
        .map(registeredAlertWithMatchesOut -> convert(
            registeredAlertWithMatchesOut,
            parsedAlertMessagesMap.get(registeredAlertWithMatchesOut.getAlertId())))
        .collect(toList());
  }

  private String getBatchName(Collection<ParsedAlertMessage> parsedAlertMessages) {
    return parsedAlertMessages
        .stream()
        .findAny()
        .map(ParsedAlertMessage::getBatchName)
        .orElseThrow(() -> new IllegalArgumentException("BatchName should be provided"));
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
        .parsedMessageData(parsedAlertMessage.getParsedMessageData())
        .messageName(registeredAlertWithMatchesOut.getAlertId())
        .alertName(registeredAlertWithMatchesOut.getAlertName())
        .systemId(parsedAlertMessage.getSystemId())
        .matches(matches)
        .build();
  }

  private static Match convert(RegisteredMatchOut registeredMatchOut, Hit hit) {
    return Match
        .builder()
        .hitName(registeredMatchOut.getMatchId())
        .matchName(registeredMatchOut.getMatchName())
        .payloads(hit.getPayloads())
        .build();
  }

  private static AlertWithMatchesIn convert(ParsedAlertMessage parsedAlertMessage) {
    return AlertWithMatchesIn
        .builder()
        .status(AlertStatusIn.SUCCESS)//TODO: What should be the origin of this value?
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
