package com.silenteight.sens.webapp.backend.domain.decisiontree;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class DecisionTreeView {

  private final long id;
  @NonNull
  private final String name;
  @NonNull
  private final String status;
  @NonNull
  private final String modelName;
}
