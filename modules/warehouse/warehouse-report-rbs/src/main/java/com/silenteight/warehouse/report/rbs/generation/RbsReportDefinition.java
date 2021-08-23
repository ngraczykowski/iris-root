package com.silenteight.warehouse.report.rbs.generation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toUnmodifiableList;

@AllArgsConstructor
@Data
public class RbsReportDefinition {

  private static final String DELIMITER = "_";

  @NotBlank
  private final String dateFieldName;
  @Valid
  @NonNull
  private final List<ColumnProperties> columns;
  @Valid
  @NonNull
  private final List<GroupingColumnProperties> groupingColumns;

  List<String> getListOfFields() {
    List<String> fields = new ArrayList<>();
    fields.addAll(columns.stream().map(Column::getName).collect(toUnmodifiableList()));
    fields.addAll(groupingColumns
        .stream()
        .map(GroupingColumnProperties::getName)
        .collect(toUnmodifiableList()));

    return fields;
  }

  List<String> getListOfStaticFields() {
    return getStaticColumns().stream().map(Column::getName).collect(toUnmodifiableList());
  }

  List<String> getListOfLabels() {
    List<String> result = new ArrayList<>();
    getStaticColumns().forEach(column -> result.addAll(column.getLabels()));
    result.add("matches_count");

    List<String> columnsLabels = groupingColumns
        .stream()
        .map(this::getColumnsLabels)
        .flatMap(Collection::stream)
        .collect(toList());

    result.addAll(columnsLabels);
    return result;
  }

  private List<String> getColumnsLabels(GroupingColumnProperties groupingColumnProperties) {
    String name = groupingColumnProperties.getLabel();
    return groupingColumnProperties.getGroupingValues().stream()
        .map(e -> name + DELIMITER + e.getValue())
        .collect(toList());
  }

  private List<Column> getStaticColumns() {
    return new ArrayList<>(columns);
  }
}
