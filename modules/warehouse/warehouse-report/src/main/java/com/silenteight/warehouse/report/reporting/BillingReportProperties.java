package com.silenteight.warehouse.report.reporting;

import lombok.AllArgsConstructor;
import lombok.Builder.Default;
import lombok.Getter;

import com.silenteight.warehouse.indexer.query.common.QueryFilter;

import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static java.util.Collections.emptyList;
import static java.util.List.of;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;

@AllArgsConstructor
@ConstructorBinding
@Getter
@Validated
public class BillingReportProperties {

  @NotBlank
  private final String yearMonthLabel;
  @NotBlank
  private final String dateFieldName;
  @NotBlank
  private final String yearFieldName;
  @NotBlank
  private final String monthFieldName;
  @Valid
  @NotNull
  private final TransposeColumnProperties transposeColumn;
  @Valid
  @Nullable
  private final List<FilterProperties> filters;
  @Default
  private boolean useSqlReports;
  @Nullable
  private List<String> sqlTemplates;
  @Nullable
  private String selectSqlQuery;

  public List<String> getDateColumnsLabel() {
    return of(yearFieldName, monthFieldName);
  }

  public List<String> getLabels() {
    List<String> labels = new ArrayList<>();
    labels.add(yearMonthLabel);
    labels.addAll(getTransposeColumn().getLabels());
    return labels;
  }

  public List<String> getListOfFields() {
    String name = transposeColumn.getName();
    return List.of(monthFieldName, yearFieldName, name);
  }

  public List<QueryFilter> getQueryFilters() {
    if (isNull(getFilters()))
      return emptyList();

    return getFilters().stream()
        .map(FilterProperties::toQueryFilter)
        .collect(toList());
  }
}
