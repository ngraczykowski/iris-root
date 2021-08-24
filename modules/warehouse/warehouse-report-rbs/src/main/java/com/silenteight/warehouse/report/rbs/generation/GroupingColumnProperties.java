package com.silenteight.warehouse.report.rbs.generation;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ConstructorBinding
@AllArgsConstructor
@Getter
class GroupingColumnProperties {

  private static final String EMPTY_DELIMITER = "";
  private static final String COUNTER = "count";

  @NotBlank
  private final String name;
  @NotBlank
  private final String label;
  @NotNull
  private final List<GroupingValues> groupingValues;
}
