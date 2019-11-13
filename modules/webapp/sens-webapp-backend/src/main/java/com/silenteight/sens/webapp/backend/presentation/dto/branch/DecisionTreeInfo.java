package com.silenteight.sens.webapp.backend.presentation.dto.branch;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class DecisionTreeInfo {

  @NonNull
  private final Long id;
  @NonNull
  private final String name;
}
