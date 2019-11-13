package com.silenteight.sens.webapp.backend.presentation.dto.dashboard;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class DashboardJobSearchFilterDto {

  @NonNull
  private final String jobName;
  private final boolean details;
}
