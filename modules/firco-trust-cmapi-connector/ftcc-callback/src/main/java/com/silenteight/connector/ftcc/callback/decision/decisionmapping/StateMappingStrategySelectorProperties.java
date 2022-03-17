package com.silenteight.connector.ftcc.callback.decision.decisionmapping;

import lombok.Data;

import com.silenteight.connector.ftcc.callback.decision.decisionmode.DecisionMode;

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
