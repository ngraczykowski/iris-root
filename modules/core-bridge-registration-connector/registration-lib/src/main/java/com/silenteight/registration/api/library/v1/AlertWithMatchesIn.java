package com.silenteight.registration.api.library.v1;

import lombok.Builder;
import lombok.Value;

import com.silenteight.registration.internal.proto.v1.AlertStatus;
import com.silenteight.registration.internal.proto.v1.AlertWithMatches;

import java.util.List;
import java.util.stream.Collectors;

@Value
@Builder
public class AlertWithMatchesIn {

  String alertId;
  AlertStatusIn status;

  @Builder.Default
  List<MatchIn> matches = List.of();

  AlertWithMatches toAlertWithMatches() {
    return AlertWithMatches.newBuilder()
        .setAlertId(alertId)
        .setStatus(AlertStatus.valueOf(status.name().toUpperCase()))
        .addAllMatches(matches.stream()
            .map(MatchIn::toMatch)
            .collect(Collectors.toList()))
        .build();
  }
}
