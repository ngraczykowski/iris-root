package com.silenteight.warehouse.report.rbs.generation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toUnmodifiableList;
import static java.util.stream.Stream.of;

@ConstructorBinding
@AllArgsConstructor
@Getter
class GroupingColumnProperties implements Column {

  private static final String DELIMITER = "_";
  private static final String EMPTY_DELIMITER = "";
  private static final String COUNTER = "count";

  @NonNull
  String name;
  @NonNull
  String label;
  @NonNull
  List<String> groupingValues;
  boolean addCounter;

  @Override
  public List<String> getLabels() {
    List<String> result = collectLabels();

    return result.stream()
        .map(value -> buildLabel(getLabel(), value))
        .collect(toUnmodifiableList());
  }

  private String buildLabel(String label, String value) {
    return of(label, value)
        .collect(joining(getDelimiterBasedOnLabel()));
  }

  private String getDelimiterBasedOnLabel() {
    return !getLabel().isEmpty() ? DELIMITER : EMPTY_DELIMITER;
  }

  private List<String> collectLabels() {
    List<String> result = new ArrayList<>();
    if (addCounter)
      result.add(COUNTER);

    result.addAll(groupingValues);
    return result;
  }
}
