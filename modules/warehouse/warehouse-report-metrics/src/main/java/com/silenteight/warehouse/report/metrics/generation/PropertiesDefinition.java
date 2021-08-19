package com.silenteight.warehouse.report.metrics.generation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import static java.util.List.of;

@AllArgsConstructor
@Data
public class PropertiesDefinition {

  @NotBlank
  String dateFieldName;
  @Valid
  @NonNull
  ColumnProperties country;
  @Valid
  @NonNull
  ColumnProperties riskType;
  @Valid
  @NonNull
  GroupingColumnProperties recommendationField;

  List<String> getFields() {
    List<String> result = getStaticFields();
    result.add(recommendationField.getName());
    return result;
  }

  @NotNull
  List<String> getStaticFields() {
    return of(country, riskType)
        .stream()
        .map(ColumnProperties::getName)
        .collect(Collectors.toList());
  }
}
