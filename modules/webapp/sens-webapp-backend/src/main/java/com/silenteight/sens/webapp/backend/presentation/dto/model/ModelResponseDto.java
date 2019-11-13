package com.silenteight.sens.webapp.backend.presentation.dto.model;

import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Data
public class ModelResponseDto {

  private final int total;
  @NonNull
  private final List<ModelDto> results;
}
