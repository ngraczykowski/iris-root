package com.silenteight.warehouse.report.reporting;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.List;

@ConstructorBinding
@AllArgsConstructor
@Getter
public class AiDecisionProperties implements Decision {

  @NonNull
  private final String field;
  @NonNull
  private final List<String> significantValues;
  @NonNull
  private final String falsePositiveValue;
}
