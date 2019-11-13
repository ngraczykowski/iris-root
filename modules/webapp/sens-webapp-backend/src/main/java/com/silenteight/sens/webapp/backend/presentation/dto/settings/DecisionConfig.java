package com.silenteight.sens.webapp.backend.presentation.dto.settings;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DecisionConfig {

  private String key;
  private String label;
  private String className;
}
