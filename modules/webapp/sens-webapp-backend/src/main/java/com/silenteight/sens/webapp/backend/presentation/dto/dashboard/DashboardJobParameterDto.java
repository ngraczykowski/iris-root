package com.silenteight.sens.webapp.backend.presentation.dto.dashboard;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class DashboardJobParameterDto {

  @NonNull
  private final String name;
  @NonNull
  private final String type;
  private final Object value;
}
