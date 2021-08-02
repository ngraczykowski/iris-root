package com.silenteight.warehouse.report.statistics.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@AllArgsConstructor
@Getter
public class AnalystDecisionProperties implements Decision {

  @NonNull
  private final String field;
  @NonNull
  private final String falsePositiveValue;
}
