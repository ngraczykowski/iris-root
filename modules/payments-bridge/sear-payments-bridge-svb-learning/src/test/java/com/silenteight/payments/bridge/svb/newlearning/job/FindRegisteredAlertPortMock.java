package com.silenteight.payments.bridge.svb.newlearning.job;

import com.silenteight.payments.bridge.ae.alertregistration.domain.FindRegisteredAlertRequest;
import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisteredAlert;
import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisteredMatch;
import com.silenteight.payments.bridge.ae.alertregistration.port.FindRegisteredAlertUseCase;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class FindRegisteredAlertPortMock implements FindRegisteredAlertUseCase {

  private final List<FindRegisteredAlertRequest> registered = List.of(FindRegisteredAlertRequest
      .builder()
      .systemId("DIN20190429085242-00061-24304")
      .messageId("87AB4899-BE5B-5E4F-E053-150A6C0A7A84")
      .build());

  @Override
  public List<RegisteredAlert> find(List<FindRegisteredAlertRequest> registeredAlert) {
    return registeredAlert
        .stream()
        .filter(registered::contains)
        .map(a -> new RegisteredAlert(
            a.getSystemId(),
            a.getMessageId(), "alerts/1", List.of(
            RegisteredMatch.builder().matchId("matchId").matchName("alerts/1/matches/1").build())))
        .collect(
            toList());
  }
}
