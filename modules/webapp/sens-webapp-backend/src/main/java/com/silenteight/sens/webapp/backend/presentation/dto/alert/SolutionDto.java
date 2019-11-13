package com.silenteight.sens.webapp.backend.presentation.dto.alert;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.Instant;

@Data
@RequiredArgsConstructor
public class SolutionDto {

  private final String externalId;
  private final String discriminator;
  private final String decision;
  private final String comment;
  private final Instant date;
  private final Boolean isOutdated;

  public SolutionDto(
      String externalId, String discriminator, String decision, String comment, Instant date) {
    this(externalId, discriminator, decision, comment, date, false);
  }
}
