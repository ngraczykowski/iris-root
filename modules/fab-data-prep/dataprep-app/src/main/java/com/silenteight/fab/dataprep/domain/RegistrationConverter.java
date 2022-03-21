package com.silenteight.fab.dataprep.domain;

import lombok.experimental.UtilityClass;

import com.silenteight.fab.dataprep.domain.model.AlertErrorDescription;
import com.silenteight.fab.dataprep.domain.model.AlertStatus;
import com.silenteight.fab.dataprep.domain.model.ParsedAlertMessage;
import com.silenteight.fab.dataprep.domain.model.ParsedAlertMessage.Hit;
import com.silenteight.fab.dataprep.domain.model.RegisteredAlert;
import com.silenteight.fab.dataprep.domain.model.RegisteredAlert.Match;
import com.silenteight.registration.api.library.v1.*;

import java.util.Collection;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

@UtilityClass
class RegistrationConverter {

  static String getBatchName(Collection<ParsedAlertMessage> parsedAlertMessages) {
    return parsedAlertMessages
        .stream()
        .findAny()
        .map(ParsedAlertMessage::getBatchName)
        .orElseThrow(() -> new IllegalArgumentException("BatchName should be provided"));
  }

  static RegisteredAlert convert(
      RegisteredAlertWithMatchesOut registeredAlertWithMatchesOut,
      ParsedAlertMessage parsedAlertMessage) {
    List<Match> matches = registeredAlertWithMatchesOut
        .getRegisteredMatches()
        .stream()
        .map(registeredMatchOut -> convert(
            registeredMatchOut, parsedAlertMessage.getHit(registeredMatchOut.getMatchId())))
        .collect(toList());

    return createRegisteredAlertBuilder(registeredAlertWithMatchesOut)
        .batchName(parsedAlertMessage.getBatchName())
        .parsedMessageData(parsedAlertMessage.getParsedMessageData())
        .systemId(parsedAlertMessage.getSystemId())
        .matches(matches)
        .build();
  }

  static RegisteredAlert createFailedRegisteredAlert(
      RegisteredAlertWithMatchesOut registeredAlertWithMatchesOut,
      String batchName,
      AlertErrorDescription errorDescription) {

    return createRegisteredAlertBuilder(registeredAlertWithMatchesOut)
        .batchName(batchName)
        .errorDescription(errorDescription)
        .matches(emptyList())
        .build();
  }

  private static RegisteredAlert.RegisteredAlertBuilder createRegisteredAlertBuilder(
      RegisteredAlertWithMatchesOut registeredAlertWithMatchesOut) {

    return RegisteredAlert
        .builder()
        .messageName(registeredAlertWithMatchesOut.getAlertId())
        .alertName(registeredAlertWithMatchesOut.getAlertName())
        .status(AlertStatus.valueOf(registeredAlertWithMatchesOut.getAlertStatus().name()));
  }

  static Match convert(RegisteredMatchOut registeredMatchOut, Hit hit) {
    return Match
        .builder()
        .hitName(registeredMatchOut.getMatchId())
        .matchName(registeredMatchOut.getMatchName())
        .payloads(hit.getPayloads())
        .build();
  }

  static AlertWithMatchesIn convert(ParsedAlertMessage parsedAlertMessage) {
    return AlertWithMatchesIn
        .builder()
        .status(AlertStatusIn.SUCCESS)
        .alertId(parsedAlertMessage.getMessageName())
        .matches(parsedAlertMessage
            .getHits()
            .values()
            .stream()
            .map(RegistrationConverter::convert)
            .collect(toList())).build();
  }

  static AlertWithMatchesIn createFailedAlertWithMatchesIn(
      String alertName,
      AlertErrorDescription errorDescription) {
    return AlertWithMatchesIn
        .builder()
        .status(AlertStatusIn.FAILURE)
        .errorDescription(errorDescription.getDescription())
        .alertId(alertName)
        .matches(emptyList())
        .build();
  }

  private static MatchIn convert(Hit hit) {
    return MatchIn.builder().matchId(hit.getHitName()).build();
  }
}
