package com.silenteight.bridge.core.registration.domain.model;

import lombok.Builder;

import java.util.List;

public record RegistrationAlert(
    String id,
    String name,
    Status status,
    List<RegistrationMatch> matches
) {

  @Builder
  public RegistrationAlert {}

  public static record RegistrationMatch(
      String id,
      String name
  ) {

    @Builder
    public RegistrationMatch {}
  }

  public enum Status {
    FAILURE, SUCCESS
  }
}
