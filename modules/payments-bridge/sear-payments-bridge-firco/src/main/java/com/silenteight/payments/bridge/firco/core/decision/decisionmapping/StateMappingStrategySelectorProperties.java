package com.silenteight.payments.bridge.firco.core.decision.decisionmapping;

import lombok.Data;

import com.silenteight.payments.bridge.firco.core.decision.decisionmode.DecisionMode;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
@Validated
@ConfigurationProperties(prefix = "pb.firco.decision.decision-mapping.selector")
class StateMappingStrategySelectorProperties {

  private Map<DecisionMode, String> decisionModeMappingStrategies = new LinkedHashMap<>();

  StateMappingStrategySelectorProperties() {
    decisionModeMappingStrategies.putAll(Map.of(
        DecisionMode.RECOMMENDATION, "recommendation",
        DecisionMode.AUTO_DECISION, "autoDecision",
        DecisionMode.RISK_AUTO_DECISION, "autoDecision"
    ));
  }
}
