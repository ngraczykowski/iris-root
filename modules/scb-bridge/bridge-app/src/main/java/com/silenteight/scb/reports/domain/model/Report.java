package com.silenteight.scb.reports.domain.model;

import lombok.Builder;
import lombok.NonNull;

import java.time.OffsetDateTime;

public record Report(AlertData alertData) {

  public static record AlertData(
      @NonNull String id,
      @NonNull String alertName,
      @NonNull String analystDecision,
      @NonNull String analystReason,
      @NonNull OffsetDateTime analystDecisionModifiedDateTime
  ) {

    @Builder
    public AlertData {}
  }
}
