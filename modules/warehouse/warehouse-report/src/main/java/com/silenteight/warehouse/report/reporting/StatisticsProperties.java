package com.silenteight.warehouse.report.reporting;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@ConstructorBinding
@Getter
@Validated
public class StatisticsProperties {

  @NotBlank
  private final String allAlertsQuery;
  @NotBlank
  private final String solvedAlertsQuery;
  @NotBlank
  private final String aiFalsePositiveQuery;
  @NotBlank
  private final String analystFalsePositiveQuery;

}
