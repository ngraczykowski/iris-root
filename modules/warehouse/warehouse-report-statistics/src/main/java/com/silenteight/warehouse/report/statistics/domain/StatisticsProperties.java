package com.silenteight.warehouse.report.statistics.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import static java.util.List.of;
import static java.util.stream.Collectors.toList;

@AllArgsConstructor
@ConstructorBinding
@Getter
@Validated
@ConfigurationProperties(prefix = "warehouse.report.statistics")
class StatisticsProperties {

  @NotBlank
  private final String dateFieldName;
  @Valid
  @NonNull
  private final AiDecisionProperties aiDecision;
  @Valid
  @NonNull
  private final AnalystDecisionProperties analystDecision;

  public List<String> getFields() {
    return of(aiDecision, analystDecision)
        .stream()
        .map(Decision::getField)
        .collect(toList());
  }
}
