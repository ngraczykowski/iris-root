package com.silenteight.sens.webapp.backend.presentation.dto.match;

import lombok.Data;
import lombok.NonNull;

import org.springframework.data.domain.Page;

@Data
public class MatchResponseDto {

  @NonNull
  private final Page<MatchDto> matches;
}
