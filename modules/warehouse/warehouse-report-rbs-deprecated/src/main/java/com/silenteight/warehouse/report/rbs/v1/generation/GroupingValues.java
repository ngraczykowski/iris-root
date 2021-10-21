package com.silenteight.warehouse.report.rbs.v1.generation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@AllArgsConstructor
@Getter
public class GroupingValues {
  @NonNull
  String value;
  @NonNull
  String label;
}
