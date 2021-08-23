package com.silenteight.warehouse.report.metrics.generation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;

import static java.util.List.of;

@AllArgsConstructor
@Data
public class PropertiesDefinition {

  @Valid
  @NonNull
  GroupingColumnProperties dateField;
  @Valid
  @NonNull
  GroupingColumnProperties country;
  @Valid
  @NonNull
  GroupingColumnProperties riskType;
  @Valid
  @NonNull
  GroupingColumnProperties hitType;
  @Valid
  @NonNull
  ColumnProperties recommendationField;
  @Valid
  @NonNull
  ColumnProperties analystDecisionField;
  @Valid
  @NonNull
  ColumnProperties qaDecisionField;

  List<String> getFields() {
    List<String> fields = getStaticFields();
    fields.add(recommendationField.getName());
    fields.add(analystDecisionField.getName());
    fields.add(qaDecisionField.getName());
    return fields;
  }

  @NotNull
  List<String> getStaticFields() {
    return of(country, riskType, dateField, hitType)
        .stream()
        .map(GroupingColumnProperties::getName)
        .collect(Collectors.toList());
  }

  List<GroupingColumnProperties> getGroupingColumns() {
    return of(country, riskType, dateField);
  }
}
