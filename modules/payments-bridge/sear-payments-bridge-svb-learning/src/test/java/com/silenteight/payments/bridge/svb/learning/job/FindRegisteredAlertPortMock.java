package com.silenteight.payments.bridge.svb.learning.job;

import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisteredAlert;
import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisteredMatch;
import com.silenteight.payments.bridge.ae.alertregistration.port.FindRegisteredAlertUseCase;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class FindRegisteredAlertPortMock implements FindRegisteredAlertUseCase {

  private final List<String> registered = List.of("DIN20190429085242-00061-24304");

  @Override
  public List<RegisteredAlert> find(List<String> registeredAlert) {
    return registeredAlert
        .stream()
        .filter(registered::contains)
        .map(a -> new RegisteredAlert(
            a, "alerts/1", List.of(
            RegisteredMatch.builder().matchId("matchId").matchName("alerts/1/matches/1").build())))
        .collect(
            toList());
  }
}
