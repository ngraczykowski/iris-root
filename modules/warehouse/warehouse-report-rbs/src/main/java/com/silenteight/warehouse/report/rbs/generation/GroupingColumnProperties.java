package com.silenteight.warehouse.report.rbs.generation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.List;

@ConstructorBinding
@AllArgsConstructor
@Getter
class GroupingColumnProperties {

  private static final String EMPTY_DELIMITER = "";
  private static final String COUNTER = "count";

  @NonNull
  String name;
  @NonNull
  String label;
  @NonNull
  List<GroupingValues> groupingValues;
}
