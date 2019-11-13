package com.silenteight.sens.webapp.backend.presentation.dto.dashboard;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Data
@Builder
public class DashboardJobDto {

  private final long jobInstanceId;
  @NonNull
  private final String jobName;
  @NonNull
  private final List<DashboardJobExecutionDto> jobExecutions;
}
