package com.silenteight.warehouse.report.accuracy.generation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;

import com.silenteight.warehouse.indexer.query.common.QueryFilter;
import com.silenteight.warehouse.indexer.query.streaming.FieldDefinition;
import com.silenteight.warehouse.indexer.query.streaming.ReportFieldDefinitions;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import static java.util.Collections.emptyList;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;

@AllArgsConstructor
@Builder
@Data
public class AccuracyReportDefinitionProperties {

  @NotBlank
  private String dateFieldName;
  @Valid
  @NotNull
  @Default
  private List<ColumnProperties> columns = new ArrayList<>();
  @Nullable
  @Valid
  private List<FilterProperties> filters;

  List<QueryFilter> getQueryFilters() {
    if (isNull(getFilters()))
      return emptyList();

    return getFilters().stream()
        .map(FilterProperties::toQueryFilter)
        .collect(toList());
  }

  public ReportFieldDefinitions getReportFieldsDefinition() {
    return ReportFieldDefinitions.builder()
        .fieldDefinitions(getFieldDefinition())
        .build();
  }

  private List<FieldDefinition> getFieldDefinition() {
    return columns.stream()
        .map(this::convertToFieldDefinition)
        .collect(toList());
  }

  private FieldDefinition convertToFieldDefinition(ColumnProperties columnProperties) {
    return FieldDefinition.builder()
        .name(columnProperties.getName())
        .label(columnProperties.getLabel())
        .build();
  }
}
