package com.silenteight.warehouse.report.reporting;

import lombok.AllArgsConstructor;
import lombok.Builder.Default;
import lombok.Data;

import com.silenteight.warehouse.indexer.query.common.QueryFilter;

import org.jetbrains.annotations.NotNull;

import java.util.List;
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
  private GroupingColumnPropertiesWithPatterns dateField;
  @Valid
  @NotNull
  private GroupingColumnPropertiesWithPatterns country;
  @Valid
  @NotNull
  private GroupingColumnPropertiesWithPatterns riskType;
  @Valid
  @NotNull
  private GroupingColumnPropertiesWithPatterns hitType;
  @Valid
  @NotNull
  private ColumnPropertiesWithValues recommendationField;
  @Valid
  @NotNull
  private ColumnPropertiesWithValues analystDecisionField;
  @Valid
  @NotNull
  private ColumnPropertiesWithValues qaDecisionField;
  @Valid
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

  @Default
  private boolean useSqlReports = false;
  @Nullable
  private List<String> sqlTemplates;
  @Nullable
  private String selectSqlQuery;

  public List<String> getFields() {
    List<String> fields = getStaticFields();
    fields.add(recommendationField.getName());
    fields.add(analystDecisionField.getName());
    fields.add(qaDecisionField.getName());
    return fields;
  }

  @NotNull
  public List<String> getStaticFields() {
    return Stream.of(country, riskType, dateField, hitType)
        .map(GroupingColumnPropertiesWithPatterns::getName)
        .collect(toList());
  }

  public List<GroupingColumnPropertiesWithPatterns> getGroupingColumns() {
    return of(country, riskType, dateField);
  }

  public List<QueryFilter> getQueryFilters() {
    if (isNull(getFilters()))
      return emptyList();

    return getFilters().stream()
        .map(FilterProperties::toQueryFilter)
        .collect(toList());
  }
}
