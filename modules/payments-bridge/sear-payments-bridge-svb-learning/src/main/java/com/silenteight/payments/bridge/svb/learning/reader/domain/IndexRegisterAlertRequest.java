package com.silenteight.payments.bridge.svb.learning.reader.domain;

import lombok.Builder;
import lombok.Value;

import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisteredAlert;
import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisteredMatch;
import com.silenteight.payments.bridge.svb.learning.reader.domain.exception.NoCorrespondingMatchException;
import com.silenteight.payments.bridge.warehouse.index.model.learning.IndexMatch;

import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;

import static java.util.stream.Collectors.toList;

@Value
@Builder
public class IndexRegisterAlertRequest {

  String alertId;
  String alertName;
  String systemId;
  String messageId;
  AnalystDecision analystDecision;
  String decision;
  List<IndexMatch> matches;

  public static List<IndexRegisterAlertRequest> fromLearningAlerts(
      RegisteredAlert registeredAlert, List<LearningAlert> learningAlerts) {
    return learningAlerts
        .stream()
        .filter(la -> la.getSystemId().equals(registeredAlert.getSystemId()))
        .map(alert -> IndexRegisterAlertRequest
            .builder()
            .alertId(alert.getAlertId())
            .alertName(registeredAlert.getAlertName())
            .systemId(alert.getSystemId())
            .messageId(alert.getMessageId())
            .analystDecision(alert.getAnalystDecision())
            .decision(alert.getDecision())
            .matches(createMatches(registeredAlert, alert))
            .build())
        .collect(toList());
  }

  @Nonnull
  private static List<IndexMatch> createMatches(
      RegisteredAlert registeredAlert, LearningAlert alert) {
    return registeredAlert
        .getMatches()
        .stream()
        .map(match -> createIndexMatch(alert, match))
        .collect(toList());
  }

  private static IndexMatch createIndexMatch(LearningAlert alert, RegisteredMatch match) {
    return IndexMatch
        .builder()
        .matchName(match.getMatchName())
        .matchId(match.getMatchId())
        .matchingTexts(getMatchingTextForId(alert.getMatches(), match.getMatchId()))
        .build();
  }

  @SuppressWarnings("SimplifyStreamApiCallChains")
  private static String getMatchingTextForId(List<LearningMatch> matches, String matchId) {
    return matches
        .stream()
        .filter(match -> match.getMatchId().equals(matchId))
        .findFirst()
        .orElseThrow(() -> new NoCorrespondingMatchException(
            String.format("There is no corresponding match for = %s", matchId)))
        .getMatchingTexts()
        .stream()
        .collect(Collectors.joining(", "));
  }
}
