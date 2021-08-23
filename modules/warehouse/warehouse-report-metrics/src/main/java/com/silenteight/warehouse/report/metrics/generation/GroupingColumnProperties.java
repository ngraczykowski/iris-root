package com.silenteight.warehouse.report.metrics.generation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import org.springframework.boot.context.properties.ConstructorBinding;

@AllArgsConstructor
@ConstructorBinding
@Getter
class GroupingColumnProperties implements Column {

  @NonNull
  private final String name;
  @NonNull
  private final String label;
  private final String sourcePattern;
  private final String targetPattern;

  public boolean isDateColumn() {
    return sourcePattern != null && targetPattern != null;
  }
}
