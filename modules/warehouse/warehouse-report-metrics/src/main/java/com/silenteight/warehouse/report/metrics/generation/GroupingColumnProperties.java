package com.silenteight.warehouse.report.metrics.generation;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.springframework.boot.context.properties.ConstructorBinding;

import javax.validation.constraints.NotNull;

@ConstructorBinding
@AllArgsConstructor
@Getter
class GroupingColumnProperties {

  @NotNull
  private final String name;
  @NotNull
  private final String label;
  private final String sourcePattern;
  private final String targetPattern;

  boolean isDateColumn() {
    return sourcePattern != null && targetPattern != null;
  }
}
