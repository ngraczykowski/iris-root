package com.silenteight.registration.api.library.v1;

import lombok.Builder;
import lombok.Value;

import com.silenteight.registration.internal.proto.v1.RegisterAlertsAndMatchesResponse;

import java.util.List;
import java.util.stream.Collectors;

@Value
@Builder
public class RegisterAlertsAndMatchesOut {

  @Builder.Default
  List<RegisteredAlertWithMatchesOut> registeredAlertWithMatches = List.of();

  static RegisterAlertsAndMatchesOut createFrom(RegisterAlertsAndMatchesResponse response) {
    return RegisterAlertsAndMatchesOut.builder()
        .registeredAlertWithMatches(response.getRegisteredAlertsWithMatchesList().stream()
            .map(RegisteredAlertWithMatchesOut::createFrom)
            .collect(Collectors.toList()))
        .build();
  }
}
