package com.silenteight.warehouse.report.billing.generation;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static java.util.List.of;

@AllArgsConstructor
@ConstructorBinding
@Getter
@Validated
@ConfigurationProperties(prefix = "warehouse.report.billing")
public class BillingReportProperties {

  @NotBlank
  String yearMonthLabel;
  @NotBlank
  String dateFieldName;
  @NotBlank
  String yearFieldName;
  @NotBlank
  String monthFieldName;
  @Valid
  @NotNull
  TransposeColumnProperties transposeColumn;

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
}
