package com.silenteight.warehouse.report.metrics.generation;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.List;
import javax.validation.constraints.NotNull;

import static java.util.List.of;

@ConstructorBinding
@AllArgsConstructor
@Getter
class ColumnProperties {

  @NotNull
  private final String name;
  @NotNull
  private final String positiveValue;
  @NotNull
  private final String negativeValue;

  List<String> getDecisionValues() {
    return of(positiveValue, negativeValue);
  }
}
