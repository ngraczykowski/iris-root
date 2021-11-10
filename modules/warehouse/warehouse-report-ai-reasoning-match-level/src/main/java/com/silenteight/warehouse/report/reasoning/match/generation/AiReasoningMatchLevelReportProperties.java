package com.silenteight.warehouse.report.reasoning.match.generation;

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
@ConfigurationProperties(prefix = "warehouse.report.ai-reasoning-match-level")
public class AiReasoningMatchLevelReportProperties {

  @Valid
  @NotNull
  AiReasoningMatchLevelReportDefinitionProperties production;

  @Valid
  @NotNull
  AiReasoningMatchLevelReportDefinitionProperties simulation;
}
