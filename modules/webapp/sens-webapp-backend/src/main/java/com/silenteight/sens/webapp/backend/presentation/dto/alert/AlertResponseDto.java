package com.silenteight.sens.webapp.backend.presentation.dto.alert;

import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Data
public class AlertResponseDto {

  private final long total;
  @NonNull
  private final AlertModelDto alertModel;
  @NonNull
  private final List<AlertDto> alerts;
}
