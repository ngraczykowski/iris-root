package com.silenteight.sens.webapp.backend.domain.decisiontree;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Data
@Builder
public class DecisionTreeView {

  private final long id;
  @NonNull
  private final String name;
  @NonNull
  private final List<String> activations;
}
