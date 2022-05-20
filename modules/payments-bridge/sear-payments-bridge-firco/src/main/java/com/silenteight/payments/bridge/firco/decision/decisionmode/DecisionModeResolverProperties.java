package com.silenteight.payments.bridge.firco.decision.decisionmode;

import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Data
@ConfigurationProperties(prefix = "pb.firco.decision.decision-mode")
@Validated
class DecisionModeResolverProperties {

  @NotBlank
  private String csvFilePath = "classpath:decision/decision-mode/decision-mode-by-unit.csv";
}
