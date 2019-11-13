package com.silenteight.sens.webapp.backend.presentation.exception;

import lombok.Getter;

import java.time.Instant;

@Getter
public class SolutionNotEnabledException extends RuntimeException {

  private static final long serialVersionUID = -6741994960351663138L;

  private static final String MESSAGE = "Recommendation for this alert is not yet enabled";
  private final String externalId;
  private final String discriminator;
  private final String decision;
  private final Instant createdAt;

  public SolutionNotEnabledException(
      String externalId, String discriminator, String decision, Instant createdAt) {
    super(MESSAGE);
    this.externalId = externalId;
    this.discriminator = discriminator;
    this.decision = decision;
    this.createdAt = createdAt;
  }
}
