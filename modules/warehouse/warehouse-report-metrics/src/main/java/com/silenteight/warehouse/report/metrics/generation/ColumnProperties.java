package com.silenteight.warehouse.report.metrics.generation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.List;

@ConstructorBinding
@AllArgsConstructor
@Getter
class ColumnProperties {

  @NonNull
  String name;
  @NonNull
  List<String> decisionValues;
  @NonNull
  String positiveValue;
}
