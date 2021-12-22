package com.silenteight.registration.api.library.v1;

import lombok.Builder;
import lombok.Value;

import com.silenteight.proto.registration.api.v1.AlertStatus;
import com.silenteight.proto.registration.api.v1.AlertWithMatches;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Value
@Builder
public class AlertWithMatchesIn {

  String alertId;
  AlertStatusIn status;
  String errorDescription;

  @Builder.Default
  List<MatchIn> matches = List.of();

  AlertWithMatches toAlertWithMatches() {
    return AlertWithMatches.newBuilder()
        .setAlertId(alertId)
        .setStatus(AlertStatus.valueOf(status.name().toUpperCase()))
        .setErrorDescription(Optional.ofNullable(errorDescription).orElse(""))
        .addAllMatches(matches.stream()
            .map(MatchIn::toMatch)
            .collect(Collectors.toList()))
        .build();
  }
}
