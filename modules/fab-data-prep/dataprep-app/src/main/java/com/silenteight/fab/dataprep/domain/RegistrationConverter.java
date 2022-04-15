package com.silenteight.fab.dataprep.domain;

import lombok.experimental.UtilityClass;

import com.silenteight.fab.dataprep.domain.model.*;
import com.silenteight.fab.dataprep.domain.model.ParsedAlertMessage.Hit;
import com.silenteight.fab.dataprep.domain.model.RegisteredAlert.Match;
import com.silenteight.registration.api.library.v1.*;

import java.util.Collection;
import java.util.Iterator;
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
            registeredMatchOut.getMatchName(),
            parsedAlertMessage.getHit(registeredMatchOut.getMatchId())))
        .collect(toList());

    return createRegisteredAlertBuilder(registeredAlertWithMatchesOut)
        .batchName(parsedAlertMessage.getBatchName())
        .parsedMessageData(parsedAlertMessage.getParsedMessageData())
        .systemId(parsedAlertMessage.getSystemId())
        .discriminator(parsedAlertMessage.getDiscriminator())
        .matches(matches)
        .build();
  }

  static RegisteredAlert convert(
      AlertItem alertItem,
      ParsedAlertMessage parsedAlertMessage) {
    Iterator<Hit> hitIterator = parsedAlertMessage.getHits().values().iterator();
    List<Match> matches = alertItem
        .getMatchNames()
        .stream()
        .map(matchName -> convert(matchName, hitIterator.next()))
        .collect(toList());

    return RegisteredAlert
        .builder()
        .alertName(alertItem.getAlertName())
        .batchName(parsedAlertMessage.getBatchName())
        .parsedMessageData(parsedAlertMessage.getParsedMessageData())
        .systemId(parsedAlertMessage.getSystemId())
        .discriminator(parsedAlertMessage.getDiscriminator())
        .matches(matches)
        .build();
  }

  static RegisteredAlert createFailedRegisteredAlert(
      RegisteredAlertWithMatchesOut registeredAlertWithMatchesOut,
      String batchName,
      String discriminator,
      AlertErrorDescription errorDescription) {

    return createRegisteredAlertBuilder(registeredAlertWithMatchesOut)
        .batchName(batchName)
        .errorDescription(errorDescription)
        .matches(emptyList())
        .discriminator(discriminator)
        .build();
  }

  private static RegisteredAlert.RegisteredAlertBuilder createRegisteredAlertBuilder(
      RegisteredAlertWithMatchesOut registeredAlertWithMatchesOut) {

    return RegisteredAlert
        .builder()
        .alertName(registeredAlertWithMatchesOut.getAlertName())
        .status(AlertStatus.valueOf(registeredAlertWithMatchesOut.getAlertStatus().name()));
  }

  static Match convert(String matchName, Hit hit) {
    return Match
        .builder()
        .matchName(matchName)
        .payloads(hit.getPayloads())
        .build();
  }

  static AlertWithMatchesIn convert(ParsedAlertMessage parsedAlertMessage) {
    return AlertWithMatchesIn
        .builder()
        .status(AlertStatusIn.SUCCESS)
        .alertId(parsedAlertMessage.getMessageName())
        .metadata("{}")
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
