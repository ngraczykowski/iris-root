package com.silenteight.warehouse.report.reasoning.v1.generation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import org.springframework.boot.context.properties.ConstructorBinding;

@AllArgsConstructor
@ConstructorBinding
@Getter
class ColumnProperties {

  @NonNull
  private final String name;
  @NonNull
  private final String label;
}
