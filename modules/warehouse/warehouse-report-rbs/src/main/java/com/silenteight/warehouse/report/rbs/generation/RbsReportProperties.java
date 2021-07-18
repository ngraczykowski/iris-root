package com.silenteight.warehouse.report.rbs.generation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toUnmodifiableList;

@AllArgsConstructor
@ConstructorBinding
@Getter
@Validated
@ConfigurationProperties(prefix = "warehouse.report.rbs")
public class RbsReportProperties {

  @NotBlank
  String dateFieldName;
  @Valid
  @NonNull
  List<ColumnProperties> columns;
  @Valid
  @NonNull
  List<GroupingColumnProperties> groupingColumns;

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
