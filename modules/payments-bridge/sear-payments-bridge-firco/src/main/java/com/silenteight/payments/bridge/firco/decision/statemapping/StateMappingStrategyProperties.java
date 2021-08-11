package com.silenteight.payments.bridge.firco.decision.statemapping;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
@ConfigurationProperties(prefix = "pb.firco.decision.state-mapping")
@Validated
class StateMappingStrategyProperties {

  /**
   * Map of strategy names to CSV files.
   */
  private Map<String, String> strategies = new LinkedHashMap<>();

  StateMappingStrategyProperties() {
    strategies.putAll(Map.of(
        "recommendation", "classpath:decision/state-mapping-strategy/recommendation.csv",
        "autoDecision", "classpath:decision/state-mapping-strategy/auto-decision.csv")
    );
  }
}
