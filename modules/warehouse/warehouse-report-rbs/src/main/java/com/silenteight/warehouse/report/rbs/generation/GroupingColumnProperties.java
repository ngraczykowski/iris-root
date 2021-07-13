package com.silenteight.warehouse.report.rbs.generation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;

@ConstructorBinding
@AllArgsConstructor
@Getter
class GroupingColumnProperties implements Column {

  private static final String DELIMITER = "_";
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
    List<String> result = new ArrayList<>();
    if (addCounter)
      result.add(getLabel() + DELIMITER + COUNTER);

    result.addAll(groupingValues
                      .stream()
                      .map(value -> getLabel() + DELIMITER + value)
                      .collect(toList()));

    return unmodifiableList(result);
  }
}
