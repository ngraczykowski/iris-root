package com.silenteight.warehouse.report.reporting;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.springframework.boot.context.properties.ConstructorBinding;

import javax.validation.constraints.NotNull;

@ConstructorBinding
@AllArgsConstructor
@Getter
public class LabelProperties {

  @NotNull
  private final String label;

}
