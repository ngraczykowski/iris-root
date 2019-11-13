package com.silenteight.sens.webapp.backend.presentation.dto.decisiontree;

import lombok.Data;

import javax.annotation.Nullable;

@Data
public class DecisionTreeSearchFilterDto {

  private static final boolean DEFAULT_ACTIVE = false;

  @Nullable
  private Boolean active;

  public boolean shouldReturnActive() {
    return active == null ? DEFAULT_ACTIVE : active;
  }
}
