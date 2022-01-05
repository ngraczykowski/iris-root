package com.silenteight.warehouse.report.reporting;

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
public class TransposeColumnProperties implements Column {

  private static final String DELIMITER = "_";

  @NonNull
  private final String name;
  @NonNull
  private final List<ColumnProperties> groupingValues;
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
        .map(ColumnProperties::getLabel)
        .collect(toList());
    result.add(significantSummationLabel);
    result.add(counterLabel);
    return unmodifiableList(result);
  }
}
