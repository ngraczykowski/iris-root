package com.silenteight.bridge.core.reports.domain.model;

import lombok.Builder;

import java.util.List;

public record AlertWithMatchesDto(
    String id,
    String name,
    String status,
    String metadata,
    String errorDescription,
    List<MatchDto> matches
) {

  @Builder
  public AlertWithMatchesDto {}

  public record MatchDto(
      String id,
      String name
  ) {}
}
