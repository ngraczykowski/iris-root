package com.silenteight.warehouse.report.reasoning.generation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;

import com.silenteight.warehouse.indexer.query.common.QueryFilter;

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
public class AiReasoningReportDefinitionProperties {

  @NotBlank
  private String dateFieldName;
  @Valid
  @NotNull
  @Default
  private List<ColumnProperties> columns = new ArrayList<>();
  @Nullable
  @Valid
  private List<FilterProperties> filters;

  List<String> getFieldNames() {
    return columns
        .stream()
        .map(ColumnProperties::getName)
        .collect(toList());
  }

  List<QueryFilter> getQueryFilters() {
    if (isNull(getFilters()))
      return emptyList();

    return getFilters().stream()
        .map(FilterProperties::toQueryFilter)
        .collect(toList());
  }
}
