package com.silenteight.sens.webapp.backend.presentation.dto.alert;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.Instant;

@Data
@RequiredArgsConstructor
public class DecisionDto {

  private final String decision;
  private final String comment;
  private final Instant date;
}
