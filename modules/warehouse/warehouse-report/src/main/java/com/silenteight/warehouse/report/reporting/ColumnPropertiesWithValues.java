package com.silenteight.warehouse.report.reporting;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.List;
import javax.validation.constraints.NotNull;

import static java.util.List.of;

@ConstructorBinding
@AllArgsConstructor
@Getter
public class ColumnPropertiesWithValues {

  @NotNull
  private final String name;
  @NotNull
  private final String positiveValue;
  @NotNull
  private final String negativeValue;

  public List<String> getDecisionValues() {
    return of(positiveValue, negativeValue);
  }
}
