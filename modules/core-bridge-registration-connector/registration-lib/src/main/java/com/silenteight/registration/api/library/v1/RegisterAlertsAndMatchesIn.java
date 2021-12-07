package com.silenteight.registration.api.library.v1;

import lombok.Builder;
import lombok.Value;

import com.silenteight.registration.internal.proto.v1.RegisterAlertsAndMatchesRequest;

import java.util.List;
import java.util.stream.Collectors;

@Value
@Builder
public class RegisterAlertsAndMatchesIn {

  String batchId;

  @Builder.Default
  List<AlertWithMatchesIn> alertsWithMatches = List.of();

  RegisterAlertsAndMatchesRequest toRegisterAlertsAndMatchesRequest() {
    return RegisterAlertsAndMatchesRequest.newBuilder()
        .setBatchId(batchId)
        .addAllAlertsWithMatches(alertsWithMatches.stream()
            .map(AlertWithMatchesIn::toAlertWithMatches)
            .collect(Collectors.toList()))
        .build();
  }
}
