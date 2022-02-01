package com.silenteight.payments.bridge.svb.learning.reader.domain;

import lombok.Builder;
import lombok.Value;

import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisteredAlert;
import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisteredMatch;

import java.util.List;

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
  List<String> matchNames;

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
            .matchNames(registeredAlert
                .getMatches()
                .stream()
                .map(RegisteredMatch::getMatchName)
                .collect(toList()))
            .build())
        .collect(toList());
  }
}
