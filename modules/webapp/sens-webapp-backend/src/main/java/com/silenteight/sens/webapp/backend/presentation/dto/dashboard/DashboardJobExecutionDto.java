package com.silenteight.sens.webapp.backend.presentation.dto.dashboard;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.Instant;
import java.util.List;

@Data
@Builder
public class DashboardJobExecutionDto {

  private final long jobExecutionId;
  @NonNull
  private final Instant createTime;
  private final Instant startTime;
  private final Instant endTime;
  @NonNull
  private final String status;
  @NonNull
  private final String exitCode;
  private final String exitMessage;
  private final List<DashboardJobParameterDto> jobParameters;
}
