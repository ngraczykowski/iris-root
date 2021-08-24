package com.silenteight.warehouse.report.metrics.generation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.List;

import static java.util.List.of;

@ConstructorBinding
@AllArgsConstructor
@Getter
class ColumnProperties {

  @NonNull
  String name;
  @NonNull
  String positiveValue;
  @NonNull
  String negativeValue;

  List<String> getDecisionValues() {
    return of(positiveValue, negativeValue);
  }
}
