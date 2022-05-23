package com.silenteight.payments.bridge.svb.learning.job;

import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisteredAlert;
import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisteredMatch;
import com.silenteight.payments.bridge.ae.alertregistration.port.FindRegisteredAlertUseCase;

import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

public class FindRegisteredAlertPortMock implements FindRegisteredAlertUseCase {

  private final List<String> registered = List.of("DIN20190429085242-00061-24304");

  @Override
  public List<RegisteredAlert> find(List<String> registeredAlert) {
    return registeredAlert
        .stream()
        .filter(registered::contains)
        .map(a -> new RegisteredAlert(UUID.randomUUID().toString(),
            a, "alerts/1", List.of(
            RegisteredMatch
                .builder()
                .matchId("shrekMatchId(ORIGINATOR, #1)")
                .matchName("alerts/1/matches/1")
                .build())))
        .collect(
            toList());
  }
}
