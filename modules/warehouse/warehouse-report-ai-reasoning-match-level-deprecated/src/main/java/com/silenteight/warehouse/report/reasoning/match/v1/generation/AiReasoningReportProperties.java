package com.silenteight.warehouse.report.reasoning.match.v1.generation;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@ConstructorBinding
@Getter
@Validated
@ConfigurationProperties(prefix = "warehouse.report.ai-reasoning-match-level-v1")
public class AiReasoningReportProperties {

  @Valid
  @NotNull
  AiReasoningReportDefinitionProperties production;
}
