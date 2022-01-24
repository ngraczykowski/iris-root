package com.silenteight.bridge.core.registration.domain.model;

import lombok.Builder;

import com.silenteight.bridge.core.registration.domain.model.Alert.Status;

import java.util.List;

public record AlertWithMatches(
    String id,
    String name,
    Status status,
    String metadata,
    String errorDescription,
    List<Match> matches
) {

  @Builder
  public AlertWithMatches {}

  public record Match(
      String id,
      String name
  ) {}
}
