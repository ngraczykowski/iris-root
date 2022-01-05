package com.silenteight.warehouse.report.accuracy.generation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import org.springframework.boot.context.properties.ConstructorBinding;

@AllArgsConstructor
@ConstructorBinding
@Getter
@Deprecated(since = "2.0.0")
class ColumnProperties {

  @NonNull
  private final String name;
  @NonNull
  private final String label;
}
