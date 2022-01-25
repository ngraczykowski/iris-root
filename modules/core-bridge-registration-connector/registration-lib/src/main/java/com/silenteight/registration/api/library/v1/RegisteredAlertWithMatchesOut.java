package com.silenteight.registration.api.library.v1;

import lombok.Builder;
import lombok.Value;

import com.silenteight.proto.registration.api.v1.RegisteredAlertWithMatches;

import java.util.List;
import java.util.stream.Collectors;

@Value
@Builder
public class RegisteredAlertWithMatchesOut {

  String alertId;
  String alertName;
  AlertStatusOut alertStatus;

  @Builder.Default
  List<RegisteredMatchOut> registeredMatches = List.of();

  static RegisteredAlertWithMatchesOut createFrom(RegisteredAlertWithMatches input) {
    return RegisteredAlertWithMatchesOut.builder()
        .alertId(input.getAlertId())
        .alertName(input.getAlertName())
        .alertStatus(AlertStatusOut.valueOf(input.getAlertStatus().name()))
        .registeredMatches(input.getRegisteredMatchesList().stream()
            .map(RegisteredMatchOut::createFrom)
            .collect(Collectors.toList()))
        .build();
  }
}
