package com.silenteight.scb.ingest.domain.model;

import lombok.Builder;
import lombok.Value;

import java.util.Collections;
import java.util.List;

@Value
@Builder
public class RegistrationResponse {

  @Builder.Default
  List<RegisteredAlertWithMatches> registeredAlertWithMatches = List.of();

  public static RegistrationResponse empty() {
    return new RegistrationResponse(Collections.emptyList());
  }

  @Value
  @Builder
  public static class RegisteredAlertWithMatches {

    String alertId;
    String alertName;
    AlertStatus alertStatus;

    @Builder.Default
    List<RegisteredMatch> registeredMatches = List.of();
  }

  @Value
  @Builder
  public static class RegisteredMatch {

    String matchId;
    String matchName;
  }
}
