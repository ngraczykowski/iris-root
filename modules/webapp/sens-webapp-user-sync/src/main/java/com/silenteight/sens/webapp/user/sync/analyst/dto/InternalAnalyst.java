package com.silenteight.sens.webapp.user.sync.analyst.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class InternalAnalyst implements Analyst {

  @NonNull
  private final String userName;
  private final String displayName;
}
