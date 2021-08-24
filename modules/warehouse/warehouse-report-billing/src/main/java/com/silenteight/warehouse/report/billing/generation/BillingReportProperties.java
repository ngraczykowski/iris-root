package com.silenteight.warehouse.report.billing.generation;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.silenteight.warehouse.indexer.query.grouping.QueryFilter;

import org.springframework.boot.context.properties.ConfigurationProperties;
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
@ConfigurationProperties(prefix = "warehouse.report.billing")
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
  @Nullable
  private final List<FilterProperties> filters;

  List<String> getDateColumnsLabel() {
    return of(yearFieldName, monthFieldName);
  }

  List<String> getLabels() {
    List<String> labels = new ArrayList<>();
    labels.add(yearMonthLabel);
    labels.addAll(getTransposeColumn().getLabels());
    return labels;
  }

  List<String> getListOfFields() {
    String name = transposeColumn.getName();
    return List.of(monthFieldName, yearFieldName, name);
  }

  List<QueryFilter> getQueryFilters() {
    if (isNull(getFilters()))
      return emptyList();

    return getFilters().stream()
        .map(FilterProperties::toQueryFilter)
        .collect(toList());
  }
}
