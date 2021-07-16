package com.silenteight.warehouse.report.billing.generation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;

import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;

@ConstructorBinding
@AllArgsConstructor
@Getter
class GroupingColumnProperties implements Column {

  private static final String DELIMITER = "_";

  @NonNull
  private final String name;
  @NonNull
  private final String label;
  @NonNull
  private final List<String> groupingValues;
  @Nullable
  private final String counterSuffix;

  @Override
  public List<String> getLabels() {
    List<String> result = new ArrayList<>();
    if (isAddCounter())
      result.add(getLabel() + DELIMITER + counterSuffix);

    result.addAll(
        groupingValues.stream().map(value -> getLabel() + DELIMITER + value).collect(toList()));

    return unmodifiableList(result);
  }

  boolean isAddCounter() {
    return counterSuffix != null;
  }
}
