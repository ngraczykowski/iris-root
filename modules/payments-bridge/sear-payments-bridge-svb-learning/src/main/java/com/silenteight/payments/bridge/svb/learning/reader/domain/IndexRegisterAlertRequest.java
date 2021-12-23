package com.silenteight.payments.bridge.svb.learning.reader.domain;

import lombok.Builder;
import lombok.Value;

import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisteredMatch;

import java.util.List;
import java.util.NoSuchElementException;

import static java.util.stream.Collectors.toList;

@Value
@Builder
public class IndexRegisterAlertRequest {

  LearningAlert learningAlert;
  List<String> matchNames;

  public static IndexRegisterAlertRequest fromLearningAlerts(
      RegisteredAlert registeredAlert, List<LearningAlert> learningAlerts) {
    var registeredDiscriminator = registeredAlert.getDiscriminator();
    var learningAlert = learningAlerts
        .stream()
        .filter(la -> la.getDiscriminator().equals(registeredDiscriminator))
        .findFirst();

    if (learningAlert.isEmpty())
      throw new NoSuchElementException("There is no corresponding alert");

    var alert = learningAlert.get();
    alert.setAlertName(registeredAlert.getAlertName());
    return IndexRegisterAlertRequest
        .builder()
        .learningAlert(alert)
        .matchNames(registeredAlert
            .getMatches()
            .stream()
            .map(RegisteredMatch::getMatchId)
            .collect(toList()))
        .build();
  }
}
