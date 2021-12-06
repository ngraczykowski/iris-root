package com.silenteight.warehouse.report.metrics.v1.generation;

import lombok.AllArgsConstructor;
import lombok.Data;

import com.silenteight.warehouse.indexer.query.common.QueryFilter;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import javax.validation.Valid;

import static java.util.Collections.emptyList;
import static java.util.List.of;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;

@AllArgsConstructor
@Data
public class PropertiesDefinition {

  @Valid
  @NotNull
  private GroupingColumnProperties dateField;
  @Valid
  @NotNull
  private GroupingColumnProperties country;
  @Valid
  @NotNull
  private GroupingColumnProperties riskType;
  @Valid
  @NotNull
  private GroupingColumnProperties hitType;
  @Valid
  @NotNull
  private ColumnProperties recommendationField;
  @Valid
  @NotNull
  private ColumnProperties analystDecisionField;
  @Valid
  @NotNull
  private ColumnProperties qaDecisionField;
  @Nullable
  private List<FilterProperties> filters;
  @Valid
  @NotNull
  private LabelProperties efficiency;
  @Valid
  @NotNull
  private LabelProperties ptpEffectiveness;
  @Valid
  @NotNull
  private LabelProperties fpEffectiveness;

  List<String> getFields() {
    List<String> fields = getStaticFields();
    fields.add(recommendationField.getName());
    fields.add(analystDecisionField.getName());
    fields.add(qaDecisionField.getName());
    return fields;
  }

  @NotNull
  List<String> getStaticFields() {
    return Stream.of(country, riskType, dateField, hitType)
        .map(GroupingColumnProperties::getName)
        .collect(Collectors.toList());
  }

  List<GroupingColumnProperties> getGroupingColumns() {
    return of(country, riskType, dateField);
  }

  List<QueryFilter> getQueryFilters() {
    if (isNull(getFilters()))
      return emptyList();

    return getFilters().stream()
        .map(FilterProperties::toQueryFilter)
        .collect(toList());
  }
}
