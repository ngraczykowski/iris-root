package com.silenteight.sens.webapp.backend.presentation.dto.alert;

import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Data
public class AlertDto {

  @NonNull
  private final Long id;
  @NonNull
  private final String externalId;
  @NonNull
  private final List<Object> alertFields;

  private final DecisionDto analystSolution;
}
