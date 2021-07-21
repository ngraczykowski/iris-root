package com.silenteight.warehouse.report.rbs.generation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toUnmodifiableList;

@AllArgsConstructor
@Data
public class RbsReportDefinition {

  @NotBlank
  private final String dateFieldName;
  @Valid
  @NonNull
  private final List<ColumnProperties> columns;
  @Valid
  @NonNull
  private final List<GroupingColumnProperties> groupingColumns;

  List<String> getListOfFields() {
    return getColumns().stream().map(Column::getName).collect(toUnmodifiableList());
  }

  List<String> getListOfStaticFields() {
    return getStaticColumns().stream().map(Column::getName).collect(toUnmodifiableList());
  }

  List<String> getListOfLabels() {
    List<String> result = new ArrayList<>();
    getStaticColumns().forEach(column -> result.addAll(column.getLabels()));
    result.add("matches_count");
    getGroupingColumns().forEach(column -> result.addAll(column.getLabels()));
    return result;
  }

  private List<Column> getColumns() {
    List<Column> result = getStaticColumns();
    result.addAll(getGroupingColumns());
    return unmodifiableList(result);
  }

  private List<Column> getStaticColumns() {
    return new ArrayList<>(columns);
  }
}
