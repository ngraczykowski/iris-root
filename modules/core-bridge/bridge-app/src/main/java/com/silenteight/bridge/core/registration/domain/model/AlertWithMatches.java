package com.silenteight.bridge.core.registration.domain.model;

import lombok.Builder;

import java.util.List;

public record AlertWithMatches(
    String id,
    String name,
    AlertStatus status,
    String metadata,
    String errorDescription,
    List<Match> matches
) {

  @Builder
  public AlertWithMatches {}

  public static record Match(
      String id,
      String name
  ) {

    @Builder
    public Match {}
  }
}
