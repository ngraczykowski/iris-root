package com.silenteight.warehouse.report.rbs.generation;

import lombok.AllArgsConstructor;
import lombok.Data;

import com.silenteight.warehouse.indexer.query.common.QueryFilter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.Nullable;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static java.util.Collections.emptyList;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toUnmodifiableList;

@AllArgsConstructor
@Data
public class RbsReportDefinition {

  private static final String DELIMITER = "_";
  private static final String MATCHES_COUNT_LABEL = "Hits Count";

  @NotBlank
  private final String dateFieldName;
  @Valid
  @NotNull
  private final List<ColumnProperties> columns;
  @Valid
  @NotNull
  private final List<GroupingColumnProperties> groupingColumns;
  @Valid
  @Nullable
  private final List<FilterProperties> filters;
  @Nullable
  private final String indexName;

  List<String> getListOfFields() {
    List<String> fields = new ArrayList<>();
    fields.addAll(getColumnNames());
    fields.addAll(getGroupingColumnLabels());
    return fields;
  }

  private List<String> getColumnNames() {
    return columns.stream()
        .map(Column::getName)
        .collect(toUnmodifiableList());
  }

  private List<String> getGroupingColumnLabels() {
    return groupingColumns.stream()
        .map(GroupingColumnProperties::getName)
        .collect(toUnmodifiableList());
  }

  List<String> getListOfStaticFields() {
    return getStaticColumns().stream().map(Column::getName).collect(toUnmodifiableList());
  }

  List<String> getListOfLabels() {
    List<String> result = new ArrayList<>();
    getStaticColumns().forEach(column -> result.addAll(column.getLabels()));
    result.add(MATCHES_COUNT_LABEL);

    List<String> columnsLabels = groupingColumns
        .stream()
        .map(RbsReportDefinition::getColumnsLabels)
        .flatMap(Collection::stream)
        .collect(toList());

    result.addAll(columnsLabels);
    return result;
  }

  List<QueryFilter> getQueryFilters() {
    if (isNull(getFilters()))
      return emptyList();

    return getFilters().stream()
        .map(FilterProperties::toQueryFilter)
        .collect(toList());
  }

  private static List<String> getColumnsLabels(GroupingColumnProperties groupingColumnProperties) {
    return groupingColumnProperties.getGroupingValues().stream()
        .map(GroupingValues::getLabel)
        .collect(toList());
  }

  private List<Column> getStaticColumns() {
    return new ArrayList<>(columns);
  }
}
