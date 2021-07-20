package com.silenteight.warehouse.report.billing.generation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.List;

import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;

@ConstructorBinding
@AllArgsConstructor
@Getter
class TransposeColumnProperties implements Column {

  private static final String DELIMITER = "_";

  @NonNull
  private final String name;
  @NonNull
  private final String label;
  @NonNull
  private final List<String> groupingValues;
  @NonNull
  private final String counterLabel;
  @NonNull
  private final List<String> significantValues;
  @NonNull
  private final String significantSummationLabel;

  @Override
  public List<String> getLabels() {
    List<String> result = groupingValues
        .stream()
        .map(value -> getLabel() + DELIMITER + value)
        .collect(toList());
    result.add(significantSummationLabel);
    result.add(counterLabel);
    return unmodifiableList(result);
  }
}
